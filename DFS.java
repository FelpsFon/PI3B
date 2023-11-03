import java.util.*;

public class DFS {
  static class Vertice {
    int visitado;
    List<Integer> listaDeAdjacencia = new ArrayList<>();
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Digite a quantidade de cenários a serem analisados: ");
    int qtdCenarios = scanner.nextInt();

    for (int i = 1; i <= qtdCenarios; i++) {
      System.out.println("\nCenário #" + i + ":");

      System.out.println("\nDigite a quantidade de vértices e arestas do grafo (X Y): ");
      int qtdVertices = scanner.nextInt();
      int qtdArestas = scanner.nextInt();

      Vertice[] vertices = new Vertice[qtdVertices + 1];
      for (int j = 0; j <= qtdVertices; j++) {
        vertices[j] = new Vertice();
      }

      System.out.println("Insira as " + qtdArestas + " arestas do grafo (X Y):");
      for (int j = 0; j < qtdArestas; j++) {
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        vertices[a].listaDeAdjacencia.add(b);
        vertices[b].listaDeAdjacencia.add(a);
      }

      System.out.println("\nLista de Adjacências do Grafo:");
      imprimirGrafo(qtdVertices, vertices);

      System.out.print("Resultado do DFS: ");
      dfs(vertices, 1);
      System.out.println();
    }
  }

  static void imprimirGrafo(int qtdVertices, Vertice[] v) {
    for (int i = 1; i <= qtdVertices; i++) {
      System.out.print("Vértice " + i + " : ");
      imprimirLista(v[i].listaDeAdjacencia);
      System.out.println();
    }
  }

  static void imprimirLista(List<Integer> lista) {
    for (int i = 0; i < lista.size(); i++) {
      if (i != lista.size() - 1) {
        System.out.print(lista.get(i) + " -> ");
      } else {
        System.out.println(lista.get(i));
      }
    }
  }

  static void dfs(Vertice[] v, int noDfs) {
    v[noDfs].visitado = 1;
    System.out.print(noDfs + " ");

    for (int no : v[noDfs].listaDeAdjacencia) {
      if (v[no].visitado == 0) {
        dfs(v, no);
      }
    }
  }
}
