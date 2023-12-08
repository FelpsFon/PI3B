import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

public class LasVegas {

	public static String mazeIniciar(String maze) {
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

			// Imprime o corpo da resposta
			String responseBody = response.body();
			System.out.println("Corpo da Resposta: " + responseBody);

			return responseBody;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String mazeMovimentar(String maze, int novaPosicao) {
		// URL da API local
		String url = "http://gtm.localhost/movimentar";

		// Corpo da solicitação em formato JSON
		String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\", \"nova_posicao\": %d}", maze,
				novaPosicao);

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
			return null;
		}
	}

	public record Vertice(int id, List<Integer> adjacencia, boolean isFinal) {
	}

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

			// Imprime o corpo da resposta
			String responseBody = response.body();
			// System.out.println("Corpo da Resposta: " + responseBody);

			return responseBody + "}";
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "\n!!Erro!!\n";
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		// Comparando número de chamadas de API
		int chamadas = 0;
		long inicioLasVegas = System.currentTimeMillis();

		// Guardar labirintos carregados na API
		String labirintos = mazeListar();

		// Ingestão do labirinto desejado
		System.out.println("Labirintos disponíveis:\n" + labirintos);

		System.out.println("Insira o labirinto que deseja explorar: ");
		String maze = scan.nextLine();

		// Chamada inicial
		String response = mazeIniciar(maze);
		chamadas++;
		JSONObject json = new JSONObject(response);

		// Registro do inicio do labirinto no HashMap
		int inicio = json.getInt("pos_atual");
		List<Integer> movimentos = new ArrayList<>();
		JSONArray jsonArray = json.getJSONArray("movimentos");
		for (int i = 0; i < jsonArray.length(); i++) {
			movimentos.add(jsonArray.getInt(i));
		}

		// Movimentação para um vértice qualquer

		Random rand = new Random();
		int atual = movimentos.get(rand.nextInt(movimentos.size()));
		response = mazeMovimentar(maze, atual);
		chamadas++;
		JSONObject jsonAtual = new JSONObject(response);
		List<Integer> movimentosAtual = new ArrayList<>();
		JSONArray jsonArrayAtual = jsonAtual.getJSONArray("movimentos");
		for (int i = 0; i < jsonArrayAtual.length(); i++) {
			movimentosAtual.add(jsonArrayAtual.getInt(i));
		}
		boolean isFinalAtual = jsonAtual.getBoolean("final");
		if (isFinalAtual) {
			return;
		}
		// Define próximo vértice aleatório
		if (!movimentosAtual.isEmpty()) {
			rand = new Random();
			atual = movimentosAtual.get(rand.nextInt(movimentosAtual.size()));
		}

		// Loop While para o algoritmo Las Vegas
		while (true) {

			// Movimentação para a posição atual e adição do vértice ao HashMap
			response = mazeMovimentar(maze, atual);
			chamadas++;
			jsonAtual = new JSONObject(response);
			// Verificar se a resposta contém a mensagem de erro
			if (jsonAtual.has("detail")
					&& jsonAtual.getString("detail").equals("ID não encontrado para o labirinto em questão ou está expirado!")) {
				break; // Sai do loop while
			}
			movimentosAtual = new ArrayList<>();
			jsonArrayAtual = jsonAtual.getJSONArray("movimentos");
			for (int i = 0; i < jsonArrayAtual.length(); i++) {
				movimentosAtual.add(jsonArrayAtual.getInt(i));
			}
			isFinalAtual = jsonAtual.getBoolean("final");

			// Se o vértice atual é o final, termina o loop
			if (isFinalAtual) {
				break;
			}

			// Escolhe um adjacente aleatório
			if (!movimentosAtual.isEmpty()) {
				rand = new Random();
				atual = movimentosAtual.get(rand.nextInt(movimentosAtual.size()));
			}
		}

		long fimLasVegas = System.currentTimeMillis();

		double tempoLasVegas = (fimLasVegas - inicioLasVegas);

		System.out.println(
				"Tempo decorrido no Las Vegas: " + tempoLasVegas + " milissegundos ou " + tempoLasVegas / 1000.0 + "segundos");
		System.out.println("Numero de chamadas da API: " + chamadas);
	}
}