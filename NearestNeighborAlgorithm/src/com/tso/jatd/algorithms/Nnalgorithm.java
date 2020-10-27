package com.tso.jatd.algorithms;

import com.tso.jatd.FileUtils;
import com.tso.jatd.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase para NearestNeighborAlgorithm (NNA).
 *
 * @author Julian Tovar
 * @since 2020-10-26
 */
public class Nnalgorithm {

    private FileUtils fileUtils;
    private Double[][] distances;
    private List<Node> nodes;
    private int startNode;
    private Double totalCost;
    private int nodeCounter;

    /**
     * Constructor
     */
    public Nnalgorithm() {
        fileUtils = new FileUtils();
        nodes = new ArrayList<>();
        startNode = 0;
    }

    /**
     * Metodo generico para resolver por NNA.
     */
    public void solve() {
        //Se lee el archivo txt
        nodes = fileUtils.readFile();

        // Se calculan las distancias euclidianas
        distances = new Double[nodes.size()][nodes.size()];
        distances = calculateEucladianDistances();

        // Se obtiene el nodo inicial
        Scanner in = new Scanner(System.in);
        System.out.println("Ingresa el nodo inicial (del 1 al " + (nodes.size() + 1) + "): ");
        String inputNode = in.nextLine();
        startNode = Integer.parseInt(inputNode) - 1;

        // Se determinan los nodos a visitar
        int[] nodesVisited = calculateNodesVisited();

        // Se muestra el resultado
        System.out.println("Orden de las ciudades: ");
        int counter = 0;
        for (int nodeVisited : nodesVisited) {
            System.out.print(nodes.get(nodeVisited).getName());

            if(counter < nodesVisited.length-1) {
                System.out.print(" -> ");
            }
            counter ++;
        }
        System.out.println("\nTotal Cost: " + totalCost);
    }

    /**
     * Funcion para determinar el orden de los nodos a visitar.
     *
     * @return Lista en orden de los nodos a visitar.
     */
    private int[] calculateNodesVisited() {
        int[] visited = new int[nodes.size() + 1]; //Arreglo para almacenar los nodos ya visitados

        visited[0] = startNode; // Se guarda el nodo inicial (elegido por el usuario)
        nodeCounter = 0; // Contador de nodos totales
        totalCost = 0.0;

        // Comenzamos a revisar desde el nodo inicial
        for (int actualNode = startNode; actualNode < nodes.size(); actualNode++) {

            Double bestDistance = 100000000.0; // Distancia arbitrario default
            int bestNode = 0;

            // Revisamos cual nodo sera el proximo a visitar
            for (int nextNode = 0; nextNode < nodes.size(); nextNode++) {

                // Si el nodo no es el mismo que el que estamos revisando y aun no se ha visitado, se puede comparar.
                if (!isNodeAlreadyVisited(visited, nextNode)) {

                    // Si la distancia del nodo a revisar es menor que la ya considerada, se selecciona el nodo.
                    if (bestDistance > distances[actualNode][nextNode]) {
                        bestDistance = distances[actualNode][nextNode]; // Guardamos la distancia para comparar en la proxima iteracion.
                        bestNode = nextNode; // Guardamos el nodo con la mejor distancia para visitar despues.
                    }

                }

            }

            actualNode = bestNode - 1; // Se setea con un numero antes debido al incrementador del ciclo for
            nodeCounter++; // Contador independiente para contar el numero de nodos recorridos
            totalCost += bestDistance; // Suma el costo total

            visited[nodeCounter] = bestNode; //Se guarda el mejor nodo en los visitados

            // Si el contador de iteraciones es igual al
            if (nodeCounter == nodes.size() - 1) {
                visited[nodeCounter + 1] = startNode;
                Double finalDistance = distances[actualNode + 1][startNode];
                totalCost += finalDistance;
                break;
            }
        }
        return visited;
    }

    /**
     * Verifica si un nodo ya ha sido visitado o no.
     *
     * @param visited Lista de nodos ya visitados.
     * @param nextNode Nodo a comparar.
     * @return Verdadero o falso.
     */
    private boolean isNodeAlreadyVisited(int[] visited, int nextNode) {
        boolean alreadyVisited = false;

        for (int i = 0; i < nodeCounter+1; i++) {
            if (visited[i] == nextNode) {
                alreadyVisited = true;
                break;
            }
        }
        return alreadyVisited;
    }

    /**
     * Funcion que calcula las distancias euclidianas.
     *
     * @return Arreglo de distancias euclidianas.
     */
    private Double[][] calculateEucladianDistances() {
        Double[][] dist = new Double[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++) { //Nodo origen

            for (int j = 0; j < nodes.size(); j++) { //Node destino
                // Se obtienen coordenadas
                Double x1 = Double.valueOf(nodes.get(i).getPositionX());
                Double x2 = Double.valueOf(nodes.get(j).getPositionX());
                Double y1 = Double.valueOf(nodes.get(i).getPositionY());
                Double y2 = Double.valueOf(nodes.get(j).getPositionY());

                //Se calculan las distancias de X y Y
                Double distanceX = Math.pow((x2 - x1), 2);
                Double distanceY = Math.pow((y2 - y1), 2);

                //Se calcula la distanccia
                Double distance = Math.sqrt(distanceX + distanceY);

                //Se
                dist[i][j] = distance;
            }
        }

        return dist;
    }

}
