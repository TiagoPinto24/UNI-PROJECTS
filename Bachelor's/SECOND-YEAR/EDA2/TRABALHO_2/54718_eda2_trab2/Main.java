import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

//classe que implementa um grafo pesado, com representação por listas de adjacências
class Graph {
    int nodes; // numero de nós
    List<Edge>[] vizinhos; // listas de adjacência

    @SuppressWarnings("unchecked")
    public Graph(int nodes) {
        this.nodes = nodes;
        vizinhos = new List[nodes];
        for (int i = 0; i < nodes; ++i)
            vizinhos[i] = new LinkedList<>();
    }

    public void addEdge(int u, int v, int weight) { // adicionar uma aresta ao grafo
        vizinhos[u].add(new Edge(v, weight));
    }
}

// classe que aplica um destino e um peso a cada aresta
class Edge {
    int destination;
    int weight;

    public Edge(int destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

public class Main {

    static final int infinito = Integer.MAX_VALUE; // variavel com valor infinito

    // implementa o algoritmo de dijkstra que calcula os caminhos mais curtos de um
    // nó fonte para os restantes nós do grafo
    public static int[] dijkstra(Graph graph, int fonte) {
        int[] distancia = new int[graph.nodes];
        Arrays.fill(distancia, infinito);
        distancia[fonte] = 0;

        // fila de prioridade que ordena os elementos, a partir da primeira posição do
        // array distancia
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        queue.add(new int[] { 0, fonte });

        while (!queue.isEmpty()) { // loop até não haver mais nós na fila
            int[] current = queue.poll(); // tira o nó da queue, com menor distancia
            int menordist = current[0];
            int u = current[1];

            if (menordist > distancia[u]) // compara a distancia atual, com a distancia registrada
                continue;

            for (Edge edge : graph.vizinhos[u]) { // relaxar as arestas do nó atual
                int v = edge.destination;
                int weight = edge.weight;

                if (distancia[u] + weight < distancia[v]) {
                    distancia[v] = distancia[u] + weight;
                    queue.add(new int[] { distancia[v], v });
                }
            }
        }

        return distancia;
    }

    public static void main(String[] argv) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String[] string = buffer.readLine().split(" ");

        int L = Integer.parseInt(string[0]); // locais
        int C = Integer.parseInt(string[1]); // correntes
        int J = Integer.parseInt(string[2]); // jornadas

        Graph espaco = new Graph(L); // grafo do oceano
        int count1 = 0, count2 = 0, count3 = 0;
        while (count1 < C) { // construção do grafo
            string = buffer.readLine().split(" ");
            espaco.addEdge(Integer.parseInt(string[1]), Integer.parseInt(string[0]), Integer.parseInt(string[2]));
            espaco.addEdge(Integer.parseInt(string[0]), Integer.parseInt(string[1]), 0);
            count1++;
        }
        // leitura das dados
        int[] local1 = new int[J];
        int[] local2 = new int[J];
        int[] local3 = new int[J];
        while (count2 < J) {
            string = buffer.readLine().split(" ");
            local1[count2] = Integer.parseInt(string[0]);
            local2[count2] = Integer.parseInt(string[1]);
            local3[count2] = Integer.parseInt(string[2]);
            count2++;
        }
        // processamento dos dados
        while (count3 < J) {
            int[] distancia = dijkstra(espaco, local1[count3]);
            if (distancia[local2[count3]] >= distancia[local3[count3]]) {
                System.out.println(distancia[local3[count3]]);
            } else {
                System.out.println(distancia[local2[count3]]);
            }
            count3++;
        }
    }
}
