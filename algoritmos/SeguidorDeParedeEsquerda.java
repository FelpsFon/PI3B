package algoritmos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

public class SeguidorDeParedeEsquerda {

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
        String requestBody = String.format("{\"id\": \"usuario\", \"labirinto\": \"%s\", \"nova_posicao\": %d}", maze, novaPosicao);

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

    public record Vertice(int id, List<Integer> adjacencia, boolean isFinal, int pai) {}

    public static void main(String[] args) {

        // Comparando número de chamadas de API
        int chamadas = 0;

        long inicioSeguidorDeParedeEsquerda = System.currentTimeMillis();
        // Chamada inicial
        String maze = "very-large-maze";
        String response = mazeIniciar(maze);
        chamadas++;
        JSONObject json = new JSONObject(response);

        // Criação das estruturas de controle
        HashMap<Integer, SeguidorDeParedeEsquerda.Vertice> mapa = new HashMap<>();

        // Registro do inicio do labirinto no HashMap
        int inicio = json.getInt("pos_atual");
        List<Integer> movimentos = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("movimentos");
        for (int i = 0; i < jsonArray.length(); i++) {
            movimentos.add(jsonArray.getInt(i));
        }
        boolean isFinal = json.getBoolean("final");
        SeguidorDeParedeEsquerda.Vertice vertice = new SeguidorDeParedeEsquerda.Vertice(inicio, movimentos, isFinal, -1);
        mapa.put(inicio, vertice);

        // Movimentação
        int atual = movimentos.get(0);// Escolha o primeiro movimento para a regra da mão esquerda
        int pai = inicio;

        // Loop While para o algoritmo Seguidor de Parede
        while (true) {

            // Movimentação para a posição atual e adição do vértice ao HashMap
            response = mazeMovimentar(maze, atual);
            chamadas++;
//            pais.put(atual, pai); // Armazena o pai do vértice atual
            JSONObject jsonAtual = new JSONObject(response);
            // Verificar se a resposta contém a mensagem de erro
            if (jsonAtual.has("detail") && jsonAtual.getString("detail").equals("ID não encontrado para o labirinto em questão ou está expirado!")) {
                break;  // Sai do loop while
            }
            List<Integer> movimentosAtual = new ArrayList<>();
            JSONArray jsonArrayAtual = jsonAtual.getJSONArray("movimentos");
            for (int i = 0; i < jsonArrayAtual.length(); i++) {
                movimentosAtual.add(jsonArrayAtual.getInt(i));
            }
            boolean isFinalAtual = jsonAtual.getBoolean("final");
            vertice = new SeguidorDeParedeEsquerda.Vertice(atual, movimentosAtual, isFinalAtual, pai);
            if (!mapa.containsKey(atual)) {
                mapa.put(atual, vertice);
            }

            // Se o vértice atual é o final, termina o loop
            if (isFinalAtual) {
                // Cria uma lista para armazenar o caminho
                List<Integer> caminho = new ArrayList<>();
                int verticeFinal = atual;
                int verticesCaminho = 0; // Contador para o número de vértices no caminho

                // Rastreia o caminho de volta ao vértice inicial
                while (verticeFinal != inicio) {
                    caminho.add(0, verticeFinal);
                    verticeFinal = mapa.get(verticeFinal).pai();
                    verticesCaminho++;
                }
                // Adiciona o vértice inicial ao caminho
                caminho.add(0, inicio);
                verticesCaminho++;

                // Imprime o caminho
                System.out.println("Caminho para o vértice final: " + caminho);
                System.out.println("Número de vértices no caminho: " + verticesCaminho);
                break;
            }

            // Escolhe um adjacente à esquerda
            boolean disponivel = false;
            if (!movimentosAtual.isEmpty()) {
                pai = atual;
                for (int i = 0; i < movimentosAtual.size(); i++) { // Percorre a lista de movimentos da esquerda para a esquerda
                    int adjacente = movimentosAtual.get(i);
                    if (!mapa.containsKey(adjacente)) {
                        atual = adjacente;
                        disponivel = true;
                        break;
                    }
                }
                // Se todos os vértices adjacentes já foram visitados, volte ao vértice pai
                if (!disponivel) {
                    atual = mapa.get(atual).pai();
                }
            }
        }

        long fimSeguidorDeParedeEsquerda = System.currentTimeMillis();

        double tempoSeguidorDeParedeEsquerda = (fimSeguidorDeParedeEsquerda - inicioSeguidorDeParedeEsquerda);

        System.out.println("Tempo decorrido no Seguidor de Parede: " + tempoSeguidorDeParedeEsquerda + " milissegundos ou " + tempoSeguidorDeParedeEsquerda/1000.0 + "segundos");
        System.out.println("Numero de chamadas da API: " + chamadas);
    }
}