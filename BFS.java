import java.util.*;

class Posicao {
  int valor;
  Posicao proxima;
}

class Fila {
  int tamanho;
  Posicao primeira;
  Posicao ultima;

  boolean Vazia() {
    if (tamanho > 0)
      return false;
    else
      return true;
  }

  void enFila(int x) {
    Posicao newPosicao = new Posicao();
    newPosicao.valor = x;

    if (tamanho == 0) {
      primeira = newPosicao;
      ultima = newPosicao;
    } else {
      ultima.proxima = newPosicao;
      ultima = newPosicao;
    }
    tamanho++;
  }

  int desenFila() {
    if (Vazia())
      return -1;
    else {
      int returnValor = primeira.valor;
      primeira = primeira.proxima;
      tamanho--;
      return returnValor;
    }
  }
}

class Vertice {
  boolean visitado;
  int distancia;
  ArrayList<Integer> lista_de_adjacencia = new ArrayList<>();

  void addAresta(int v) {
    lista_de_adjacencia.add(v);
    Collections.sort(lista_de_adjacencia);
  }
}

public class BFS {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int numVertices = scanner.nextInt();
    int numArestas = scanner.nextInt();

    Vertice[] vertices = new Vertice[numVertices + 1];
    for (int i = 0; i <= numVertices; i++) {
      vertices[i] = new Vertice();
    }

    for (int i = 0; i < numArestas; i++) {
      int a = scanner.nextInt();
      int b = scanner.nextInt();
      vertices[a].addAresta(b);
      vertices[b].addAresta(a);
    }

    bfs(vertices, 6);
  }

  static void bfs(Vertice[] vertices, int raiz) {
    Fila Fila = new Fila();
    Fila.enFila(raiz);

    while (!Fila.Vazia()) {
      int atual = Fila.desenFila();
      if (!vertices[atual].visitado)
        System.out.print(" " + atual);

      vertices[atual].visitado = true;
      for (int i : vertices[atual].lista_de_adjacencia) {
        if (!vertices[i].visitado) {
          vertices[i].distancia = vertices[atual].distancia + 1;
          Fila.enFila(i);
        }
      }
    }
  }
}