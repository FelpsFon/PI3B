import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

public class Teste {

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

    Queue<Integer> fila = new LinkedList<>();
    Map<Integer, Vertice> mapaVertices = new HashMap<>();

    public void explorarLabirinto(String labirinto) {
        String resposta = mazeIniciar(labirinto);
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

                    String respostaMov = mazeMovimentar(labirinto, idAdjacente);
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
                }else{
                    fila.remove(idAdjacente);
                }

            }
        }
    }

    public static void main(String[] args) {
        Teste teste = new Teste();
        teste.explorarLabirinto("maze-sample");
    }
}
