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

    public static class Vertice {
        private final int id;
        private final List<Integer> adjacencia;
        private boolean isFinal; // Adiciona um campo para verificar se o vértice é o final

        // Construtor
        public Vertice(int id, List<Integer> adjacencia, boolean isFinal) {
            this.id = id;
            this.adjacencia = adjacencia;
            this.isFinal = isFinal;
        }

        // Getters
        public int getId() {
            return this.id;
        }

        public List<Integer> getAdjacencia() {
            return this.adjacencia;
        }

        public boolean isFinal() {
            return this.isFinal;
        }

        // Setters

        public void setFinal(boolean isFinal) {
            this.isFinal = isFinal;
        }

    }

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
                for (int adjacente : vertice.getAdjacencia()) {
                    if (!visitados.containsKey(adjacente)) {
                        fila.add(adjacente);
                        visitados.put(adjacente, atual);
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
        Teste teste = new Teste();
        String maze = "mediu-maze";
        String response = teste.mazeIniciar(maze);
        JSONObject json = new JSONObject(response);
        int inicio = json.getInt("pos_atual");
        int profundidadeMax = Integer.MAX_VALUE;
        HashMap<Integer, Vertice> mapa = new HashMap<>();
        Stack<Integer> pilha = new Stack<>();
        pilha.push(inicio);
        int profundidade = 0;

        while (!pilha.isEmpty() && profundidade < profundidadeMax) {
            int atual = pilha.peek();  // Olhe o topo da pilha, mas não remova o elemento
            if (atual != inicio) {
                response = teste.mazeMovimentar(maze, atual);
                json = new JSONObject(response);
            }
            List<Integer> movimentos = new ArrayList<>();
            JSONArray jsonArray = json.getJSONArray("movimentos");
            for (int i = 0; i < jsonArray.length(); i++) {
                movimentos.add(jsonArray.getInt(i));
            }
            boolean isFinal = json.getBoolean("final");
            Vertice vertice = new Vertice(atual, movimentos, isFinal);
            mapa.put(atual, vertice);
            if (isFinal) {
                profundidadeMax = profundidade;
            } else {
                boolean allVisited = true;
                for (int adjacente : movimentos) {
                    if (!mapa.containsKey(adjacente)) {
                        pilha.push(adjacente);
                        profundidade++;
                        allVisited = false;
                        break;
                    }
                }
                if (allVisited) {
                    pilha.pop();
                    profundidade--;
                }
            }
            System.out.println(pilha);
        }

        BFS bfs = teste.new BFS();
        List<Integer> caminho = bfs.buscar(mapa, inicio);
        System.out.println("Caminho mais curto: " + caminho);
    }
}