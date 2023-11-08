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
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  Scanner scan = new Scanner(System.in);
  Queue<Integer> fila = new LinkedList<>();
  Map<Integer, Vertice> mapaVertices = new HashMap<>();

  class Vertice {
    private int id;
    private List<Integer> adjacencia;
    private boolean visitado;

    // Construtor
    public Vertice(int id, List<Integer> adjacencia, boolean visitado) {
      this.id = id;
      this.adjacencia = adjacencia;
      this.visitado = visitado;
    }

    // Getters
    public int getId() {
      return this.id;
    }

    public List<Integer> getAdjacencia() {
      return this.adjacencia;
    }

    public boolean isVisitado() {
      return this.visitado;
    }

    // Setters
    public void setId(int id) {
      this.id = id;
    }

    public void setAdjacencia(List<Integer> adjacencia) {
      this.adjacencia = adjacencia;
    }

    public void setVisitado(boolean visitado) {
      this.visitado = visitado;
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

  public void iniciarPrograma() {
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
    String resposta = mazeIniciar(maze, userID);
    JSONObject jsonResposta = new JSONObject(resposta);

    int posAtual = jsonResposta.getInt("pos_atual");
    JSONArray movimentos = jsonResposta.getJSONArray("movimentos");

    // Converte a lista de movimentos para List<Integer>
    List<Integer> movimentosAsIntegers = new ArrayList<>();
    for (Object obj : movimentos.toList()) {
      movimentosAsIntegers.add((Integer) obj);
    }

    Vertice verticeAtual = new Vertice(posAtual, movimentosAsIntegers, false);
    mapaVertices.put(posAtual, verticeAtual);

    fila.add(posAtual);
    fila.addAll(movimentosAsIntegers);

    while (!fila.isEmpty()) {
      fila.poll();
      verticeAtual.setVisitado(true);

      for (Integer idAdjacente : verticeAtual.getAdjacencia()) {
        if (!mapaVertices.containsKey(idAdjacente)) {
          // Adicione o vértice ao mapa antes de enfileirar sua lista de adjacência

          String respostaMov = mazeMovimentar(maze, userID, idAdjacente);
          assert respostaMov != null;
          JSONObject jsonRespostaMov = new JSONObject(respostaMov);

          int posAtualMov = jsonRespostaMov.getInt("pos_atual");
          JSONArray movimentosMov = jsonRespostaMov.getJSONArray("movimentos");

          if (jsonResposta.getBoolean("final")) {
            System.out.println("Chegamos ao final do labirinto no vértice " + posAtualMov);
            return;
          }

          // Converte a lista de movimentos para List<Integer>
          List<Integer> movimentosAsIntegersMov = new ArrayList<>();
          for (Object obj : movimentosMov.toList()) {
            movimentosAsIntegersMov.add((Integer) obj);
          }

          Vertice adjacente = new Vertice(idAdjacente, movimentosAsIntegersMov, true);
          mapaVertices.put(idAdjacente, adjacente);
          fila.addAll(movimentosAsIntegersMov);
          fila.poll();
        } else {
          fila.remove(idAdjacente);
        }
      }
    }
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.iniciarPrograma();
  }
}