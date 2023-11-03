import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static Scanner scan = new Scanner(System.in);

  public static String mazeListar() {
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

      // Imprime o código de status da resposta
      // int statusCode = response.statusCode();
      // System.out.println("Código de Status: " + statusCode);

      // Imprime o corpo da resposta
      return response.body();
      // System.out.println("Corpo da Resposta: " + responseBody);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return "\n!!Erro!!\n";
    }
  }

  public static int mazeIniciar(String maze) {
    // URL da API local
    String url = "http://gtm.localhost/iniciar";

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\"}", maze);

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
      // System.out.println("Código de Status: " + statusCode);

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);

      return statusCode;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return 1;
    }
  }

  public static int mazeMovimentar(String maze) {
    // URL da API local
    String url = "http://gtm.localhost/movimentar";

    System.out.println("Para onde deseja se movimentar?");
    int mov = scan.nextInt();

    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\", \"nova_posicao\": %d}", maze, mov);

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
      // System.out.println("Código de Status: " + statusCode);

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);

      return statusCode;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return 1;
    }
  }

  public static int mazeValidar(String maze) {
    // URL da API local
    String url = "http://gtm.localhost/validar_caminho";

    List<Integer> listaDeMovimentos = new ArrayList<>();
    // Corpo da solicitação em formato JSON
    String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\", \"todos_movimentos\": %d}", maze,
        listaDeMovimentos);

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

      // Imprime o corpo da resposta
      String responseBody = response.body();
      System.out.println("Corpo da Resposta: " + responseBody);
      return 0;

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return 1;
    }
  }

  public static void main(String[] args) {
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
          case 1:
            System.out.println(labirintos);
            System.out.print("\nPressione qualquer tecla para continuar...");
            scan.nextLine(); scan.nextLine();
            break;
          case 2:
            System.out.println("Qual labirinto deseja iniciar?\nOpções:" + labirintos); scan.nextLine();
            String maze = scan.nextLine();

            int iniciarStatusCode = mazeIniciar(maze);
            if (iniciarStatusCode == 200)
              while(true)
                mazeMovimentar(maze);
            else
              System.out.println("Erro ao iniciar o labirinto!");

            System.out.print("\nPressione qualquer tecla para continuar...");
            scan.nextLine();
            break;
          case 3:
            System.out.print("\033\143"); // Limpa o console
            return;
          default:
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
}