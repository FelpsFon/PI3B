import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {

  public record Vertice(int id, List<Integer> adjacencia, boolean isFinal) {}

  class BFS {
    public List<Integer> buscar(HashMap<Integer, Vertice> mapa, int inicio) {
      HashMap<Integer, Integer> visitados = new HashMap<>();
      Queue<Integer> fila = new LinkedList<>();
      fila.add(inicio);
      visitados.put(inicio, null);
      int fim = -1;

      while (!fila.isEmpty()) {
        int atual = fila.remove();
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

      if (fim == -1) {
        return null; // Não encontrou um caminho para o final
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

  public int mazeGetStatus(String maze, String id) {
    // URL da API local
    String url = "http://gtm.localhost/iniciar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"%s\", \"labirinto\": \"%s\"}", id, maze);

    // Cria um cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Imprime o código de status da resposta
      int statusCode = response.statusCode();
      System.out.println("Código de Status: " + statusCode);

      return statusCode;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return 1;
    }
  }

  public String mazeIniciar(String maze, String id) {
    // URL da API local
    String url = "http://gtm.localhost/iniciar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"%s\", \"labirinto\": \"%s\"}", id, maze);

    // Cria um cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);

      return responseBody;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return "\n!!Erro!!\n";
    }
  }

  public String mazeListar() {
    // URL da API local
    String url = "http://gtm.localhost/labirintos";

    // Cria um cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Imprime o corpo da resposta
      String responseBody = response.body();
      //System.out.println("Corpo da Resposta: " + responseBody);

      return responseBody + "}";
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return "\n!!Erro!!\n";
    }
  }

  public String mazeMovimentar(String maze, String id, int mov) {
    // URL da API local
    String url = "http://gtm.localhost/movimentar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"%s\", \"labirinto\": \"%s\", \"nova_posicao\": %d}", id, maze, mov);

    // Cria um cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);

      return responseBody;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return "\n!!Erro!!\n";
    }
  }

  public String mazeValidarCaminho(String maze, String id, List<Integer> caminho) {
    // URL da API local
    String url = "http://gtm.localhost/validar_caminho";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"%s\", \"labirinto\": \"%s\", \"todos_movimentos\": %s}", id, maze, caminho);

    // Cria um cliente HTTP
    HttpClient client = HttpClient.newHttpClient();

    // Cria uma solicitação POST
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    try {
      // Envia a solicitação e obtém a resposta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);

      return responseBody;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return "\n!!Erro!!\n";
    }
  }

  public void iniciarPrograma(Scanner scan) {
    try {
      // Limpa o console
      System.out.print("\033\143");

      String labirintos = mazeListar();

      while (true) {
        // Imprimir menu de interação
        System.out.println("O que deseja fazer?\n  1. Listar Labirintos\n  2. Iniciar Labirinto\n  3. Sair");

        // Ler opção escolhida
        int opcao = scan.nextInt();

        switch (opcao) {
          case 1: // Listar Labirintos
            System.out.println(labirintos);
            System.out.print("\nPressione qualquer tecla para continuar...");
            scan.nextLine();
            scan.nextLine();
            break;

          case 2: // Iniciar Labirinto
            System.out.println("Qual labirinto deseja iniciar?\nOpções:" + labirintos);
            scan.nextLine();
            String maze = scan.nextLine();

            System.out.println("\nInsira um id para seu usuário: ");
            String userID = scan.nextLine();

            int iniciarStatusCode = mazeGetStatus(maze, userID);

            if (iniciarStatusCode == 200) {
              explorarLabirinto(maze, userID);
            } else {
              System.out.println("Erro ao iniciar o labirinto!");
            }
            System.out.print("\nPressione qualquer tecla para continuar...");
            scan.nextLine();
            break;

          case 3: // Encerrar programa
            System.out.print("\033\143"); // Limpa o console
            return;

          default: // Opção inválida
            System.out.println("Opção Inválida!");
            System.out.print("\nPressione qualquer tecla para continuar...");
            scan.nextLine();
            break;
        }
        // Limpa o console
        System.out.print("\033\143");
      }
    } catch (final Exception e) {
      System.err.println("Erro construindo o console!\n" + e);
    }
  }

  public void explorarLabirinto(String maze, String userID) {
    // Comparando número de chamadas de API
    int chamadas = 0;

    // Chamada inicial
    String response = mazeIniciar(maze, userID);
    chamadas++;
    JSONObject json = new JSONObject(response);

    // Criação das estruturas de controle
    HashMap<Integer, Vertice> mapa = new HashMap<>();
    HashMap<Integer, Integer> profundidade = new HashMap<>();
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
    profundidade.put(inicio, 0);

    // Adição do inicio do labirinto à pilha
    pilha.push(inicio);

    // Adiciona o primeiro vértice da lista de adjacência à pilha
    int proximo = movimentos.get(0);
    pilha.push(proximo);
    profundidade.put(proximo, 1);

    // Variável para rastrear a profundidade máxima
    int profundidadeMaxima = Integer.MAX_VALUE;

    // Tempo do DFS
    long inicioDFS = System.currentTimeMillis();
    // Loop While para o DFS
    while (!pilha.isEmpty()) {

      // Movimentação para o topo da pilha e adição do vértice ao HashMap
      int atual = pilha.peek(); // Olhe o topo da pilha, mas não remova o elemento
      response = mazeMovimentar(maze, userID, atual);
      chamadas++;
      JSONObject jsonAtual = new JSONObject(response);
      List<Integer> movimentosAtual = new ArrayList<>();
      JSONArray jsonArrayAtual = jsonAtual.getJSONArray("movimentos");
      for (int i = 0; i < jsonArrayAtual.length(); i++) {
        movimentosAtual.add(jsonArrayAtual.getInt(i));
      }
      boolean isFinalAtual = jsonAtual.getBoolean("final");
      Vertice verticeAtual = new Vertice(atual, movimentosAtual, isFinalAtual);
      mapa.put(atual, verticeAtual);

      // Se o vértice atual é o final, atualiza a profundidadeMaxima
      if (isFinalAtual) {
        profundidadeMaxima = profundidade.get(atual);
      }

      // Implementação para conferir se a lista de adjacência do vértice atual está
      // totalmente visitada
      boolean allVisited = true;
      for (int adjacente : movimentosAtual) {
        if (!mapa.containsKey(adjacente) && profundidade.get(atual) + 1 <= profundidadeMaxima) {
          pilha.push(adjacente);
          profundidade.put(adjacente, profundidade.get(atual) + 1);
          allVisited = false;
          break;
        }
      }
      if (allVisited) {
        pilha.pop();
      }
      System.out.println(pilha);
    }

    long fimDFS = System.currentTimeMillis();

    System.out.println(mapa);

    double tempoDFS = (fimDFS - inicioDFS);

    System.out.println("Tempo decorrido no DFS: " + tempoDFS + " milissegundos ou " + tempoDFS / 1000.0 + "segundos");

    BFS bfs = new BFS();
    long inicioBFS = System.currentTimeMillis();
    List<Integer> caminho = bfs.buscar(mapa, inicio);
    long fimBFS = System.currentTimeMillis();
    double tempoBFS = (fimBFS - inicioBFS);

    System.out.println("Tempo decorrido no BFS: " + tempoBFS + " milissegundos ou " + tempoBFS / 1000.0 + "segundos");
    System.out.println("Caminho mais curto: " + caminho);
    System.out.println("Numero de chamadas da API: " + chamadas);
    mazeValidarCaminho(maze, userID, caminho);
  }

  public static void main(String[] args) {
    Main main = new Main();
    Scanner scan = new Scanner(System.in);

    main.iniciarPrograma(scan);
  }
}