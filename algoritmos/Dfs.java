package algoritmos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dfs {
  private static final HttpClient client = HttpClient.newHttpClient();
  private static final Logger LOGGER = Logger.getLogger(Dfs.class.getName());

  public static String mazeIniciar(String maze) {
    // URL da API local
    String url = "http://gtm.localhost/iniciar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\"}", maze);

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response.body();
    } catch (IOException | InterruptedException e) {
      LOGGER.log(Level.SEVERE, "An exception occurred", e);
      return null;
    }
  }

  public static String mazeMovimentar(String maze, int novaPosicao) {
    // URL da API local
    String url = "http://gtm.localhost/movimentar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\", \"nova_posicao\": %d}", maze, novaPosicao);

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response.body();
    } catch (IOException | InterruptedException e) {
      LOGGER.log(Level.SEVERE, "An exception occurred", e);
      return null;
    }
  }

  public record Vertice(int id, List<Integer> adjacencia, boolean isFinal) {}

  public static class BFS {
    public List<Integer> buscar(HashMap<Integer, Vertice> mapa, int inicio) {
      HashMap<Integer, Integer> visitados = new HashMap<>();
      Queue<Integer> fila = new LinkedList<>();
      fila.add(inicio);
      visitados.put(inicio, null);
      int fim = -1;

      while (!fila.isEmpty()) {
        int atual = fila.remove();
        if (mapa.containsKey(atual)) {
          Vertice vertice = mapa.get(atual);
          if (vertice.isFinal()) {
            fim = atual;
            break;
          }
          for (int adjacente : vertice.adjacencia()) {
            if (!visitados.containsKey(adjacente)) {
              fila.add(adjacente);
              visitados.put(adjacente, atual);
            }
          }
        }
      }

      if (fim == -1) {
        return null;  // Não encontrou um caminho para o final
      }

      List<Integer> caminho = new LinkedList<>();
      Integer atual = fim;
      while (atual != null) {
        caminho.add(0, atual);
        atual = visitados.get(atual);
      }

      return caminho;
    }
  }


  public static void main(String[] args) {

    // Comparando número de chamadas de API
    int chamadas = 0;

    // Chamada inicial
    String maze = "very-large-maze";
    String response = mazeIniciar(maze);
    chamadas++;
    JSONObject json = new JSONObject(response);

    // Criação das estruturas de controle
    HashMap<Integer, Vertice> mapa = new HashMap<>();
    Stack<Integer> pilha = new Stack<>();

    // Registro do inicio do labirinto no HashMap
    int inicio = json.getInt("pos_atual");
    List<Integer> movimentos = new ArrayList<>();
    JSONArray jsonArray = json.getJSONArray("movimentos");
    for (int i = 0; i < jsonArray.length(); i++) {
      movimentos.add(jsonArray.getInt(i));
    }
    boolean isFinal = json.getBoolean("final");
    Vertice vertice = new Vertice(inicio, movimentos, isFinal);
    mapa.put(inicio, vertice);

    // Adição do inicio do labirinto à pilha
    pilha.push(inicio);

    // Adiciona o primeiro vértice da lista de adjacência à pilha
    int proximo = movimentos.get(0);
    pilha.push(proximo);

    // Tempo do DFS
    long inicioDFS = System.currentTimeMillis();
    // Loop While para o DFS
    outerloop:
    while (!pilha.isEmpty()) {

      // Movimentação para o topo da pilha e adição do vértice ao HashMap
      int atual = pilha.peek();  // Olhe o topo da pilha, mas não remova o elemento
      response = mazeMovimentar(maze, atual);
      chamadas++;
      JSONObject jsonAtual = new JSONObject(response);
      // Verificar se a resposta contém a mensagem de erro
      if (jsonAtual.has("detail") && jsonAtual.getString("detail").equals("ID não encontrado para o labirinto em questão ou está expirado!")) {
        break outerloop;  // Sai do loop while
      }
      List<Integer> movimentosAtual = new ArrayList<>();
      JSONArray jsonArrayAtual = jsonAtual.getJSONArray("movimentos");
      for (int i = 0; i < jsonArrayAtual.length(); i++) {
        movimentosAtual.add(jsonArrayAtual.getInt(i));
      }
      boolean isFinalAtual = jsonAtual.getBoolean("final");
      Vertice verticeAtual = new Vertice(atual, movimentosAtual, isFinalAtual);
      mapa.put(atual, verticeAtual);

      // Implementação para conferir se a lista de adjacência do vértice atual está totalmente visitada
      boolean allVisited = true;
      for (int adjacente : movimentosAtual) {
        if (!mapa.containsKey(adjacente)) {
          pilha.push(adjacente);
          allVisited = false;
          break;
        }
      }
      if (allVisited) {
        pilha.pop();
      }
    }

    long fimDFS = System.currentTimeMillis();

    double tempoDFS = (fimDFS - inicioDFS);

    System.out.println("Tempo decorrido no DFS: " + tempoDFS + " milissegundos ou " + tempoDFS/1000.0 + "segundos");

    BFS bfs = new BFS();
    long inicioBFS = System.currentTimeMillis();
    List<Integer> caminho = bfs.buscar(mapa, inicio);
    long fimBFS = System.currentTimeMillis();
    double tempoBFS = (fimBFS - inicioBFS);
    int nVertices = caminho.size();

    System.out.println("Tempo decorrido no BFS: " + tempoBFS + " milissegundos ou " + tempoBFS/1000.0 + "segundos");
    System.out.println("Caminho mais curto: " + caminho);
    System.out.println("Numero de chamadas da API: " + chamadas);
    System.out.println("Numero de elementos no caminho mais curto: " + nVertices);
  }
}