package com.fime.tso.algorithms;

import com.fime.tso.util.FileUtils;
import com.fime.tso.objects.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NearestInsertion {

    private FileUtils fileUtils;
    private Double[][] distances;
    private List<Node> nodes;
    private int startNode;
    private Double totalCost;
    private int nodeCounter;

    private List<Integer> subtour;
    private List<Integer> nodesRemaining;

    /**
     * Constructor
     */
    public NearestInsertion() {
        fileUtils = new FileUtils();
        nodes = new ArrayList<>();
        startNode = 0;
        subtour = new ArrayList<>();
        nodesRemaining = new ArrayList<>();
    }

    /**
     * Metodo generico para resolver por NIA (Nearest Insertion Algorithm).
     *
     */
    public void solve() {
        //Se lee el archivo txt
        nodes = getDataFromFile();

        // Se calculan las distancias euclidianas
        distances = new Double[nodes.size()][nodes.size()];
        distances = calculateEucladianDistances();

        // Se muestran las distancias
        printDistances();

        // Se obtiene el nodo inicial
        getInputInitialNode();

        subtour.add(startNode); // Se guarda el nodo inicial.
        subtour.add(calculateNextNode(startNode));
        subtour.add(startNode); // Guarda el nodo final.

        System.out.println();
        for(int i = 0 ; i < subtour.size(); i++){
            System.out.print(subtour.get(i)+1);
            if( i < subtour.size()-1) {
                System.out.print(" -> ");
            }
        }
        // Se obtienen los nodos que aun no se han visitado
        getNodesNotVisited();

        // Se calcula el proximo nodo a visitar
        while(!nodesRemaining.isEmpty()) {

            int k = getNearestNextNode();
            System.out.println("\nNode k: " + (k+1));
            subtour = insertNearestNextNode(k);
            System.out.println();
            for(int i = 0 ; i < subtour.size(); i++){
                System.out.print(subtour.get(i)+1);
                if( i < subtour.size()-1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();

            getNodesNotVisited();

        }

        // Se calcula el costo total.
        totalCost = calculateSubtourCost(subtour);

        // Se muestra el resultado
        showResults(subtour);
    }

    /**
     * Se calcula el costo de las posibles inserciones del nodo.
     *
     * @param k
     * @return
     */
    private List<Integer> insertNearestNextNode(int k) {

        List<Integer> nextSubtour = new ArrayList<>(); // Proximo subtour
        Double costNextSubtour = 1000000000000000000.0; // Costo del proximo subtour

        int pos = 1; // Posible arco de insercion

        // Se recorren los arcos
        for (int i = 0; i < subtour.size()-1; i++) {

            Double tmpCostSubtour = 1000000000000000000.0; // Cost of best node
            List<Integer> tmpNextSubtour = new ArrayList<>();

            // Se crea un nuevo subtour insertando el nodo en la posicion indicada
            for(int j = 0; j < subtour.size(); j++) {
                if (pos == j) {
                    tmpNextSubtour.add(k); // Se inserta el nuevo nodo
                    tmpNextSubtour.add(subtour.get(j));
                } else {
                    tmpNextSubtour.add(subtour.get(j));
                }
            }

            tmpCostSubtour = calculateDiferenceInsertion(pos, tmpNextSubtour); // Se calcula el costo del subtour

            if(costNextSubtour > tmpCostSubtour) {
                nextSubtour = tmpNextSubtour;
                costNextSubtour = tmpCostSubtour;
            }

            pos++;
        }

        return nextSubtour;
    }

    private int getNearestNextNode() {
        int nextK = 0; // Nearest node
        Double costNextK = 1000000000000000000.0; // Cost of best node

        for(int k = 0; k < subtour.size()-1; k++) { // Se recorren los nodos del subtour

            for(Integer nodeRemaining: nodesRemaining) { // Se recorren los nodos restantes

                if (costNextK > distances[k][nodeRemaining]) {
                    costNextK = distances[k][nodeRemaining];
                    nextK = nodeRemaining;
                }
            }
        }
        return nextK;
    }

    private int calculateNextNode(int actualNode) {
        nodeCounter = 0;

        Double bestDistance = 100000000.0; // Distancia arbitrario default
        int bestNode = 0;
        // Se revisan los posibles nodos a visitar.
        for(int nextNode = 0; nextNode < nodes.size(); nextNode++) {

            if(!isNodeAlreadyVisited(subtour, nextNode)) { // Se valida si el nodo ya ha sido visitado
                if (bestDistance > distances[actualNode][nextNode]) {
                    bestDistance = distances[actualNode][nextNode];
                    bestNode = nextNode;
                }
            }
        }

        return bestNode;
    }

    /**
     * Verifica si un nodo ya ha sido visitado o no.
     *
     * @param visited Lista de nodos ya visitados.
     * @param nextNode Nodo a comparar.
     * @return Verdadero o falso.
     */
    private boolean isNodeAlreadyVisited(List<Integer> visited, int nextNode) {
        boolean alreadyVisited = false;

        for(Integer nodeVisited : visited) {
            if(nodeVisited == nextNode) {
                alreadyVisited = true;
                break;
            }
        }

        return alreadyVisited;
    }

    /**
     * Funcion para obtener los nodos que faltan por visitar.
     */
    private void getNodesNotVisited() {
        nodesRemaining = new ArrayList<>(); // Se limpia la lista al buscar los nodos restantes
        //Se recorren todos los nodos de la lista de nodos
        for(int nodeRev = 0; nodeRev < nodes.size(); nodeRev++) {

            //Se verifica el nodo en el subtour
            if (!isNodeAlreadyVisited(subtour, nodeRev)) {
                nodesRemaining.add(nodeRev);
            }
        }
    }

    /**
     * Calcular costo de un subtour.
     *
     * @param subtourK Subtour de nodos de recorrido.
     * @return Costo del subtour.
     */
    private Double calculateSubtourCost(List<Integer> subtourK) {
        Double subTourCost = 0.0;
        for(int i = 1; i < subtourK.size(); i++) {
            subTourCost += distances[subtourK.get(i-1)][subtourK.get(i)];
        }
        return subTourCost;
    }

    /**
     * Calcular la diferencia con el nodo a insertar.
     *
     * @param posK Posicion actual del nodo insertado.
     * @param subtourK Subtour con el nodo insertado.
     * @return Obtiene el costo diferencial.
     */
    private Double calculateDiferenceInsertion(int posK, List<Integer> subtourK) {
        Double arcLong = (distances[subtourK.get(posK-1)][subtourK.get(posK)] + distances[subtourK.get(posK)][subtourK.get(posK+1)])
                - distances[subtourK.get(posK-1)][subtourK.get(posK+1)];
        System.out.println("f = c[" + (posK) + "][" + (posK+1) + "] + c[" + (posK+1) + "][" + (posK+2) + "] " +
                "- c[" + (posK) + "][" + (posK+2) + "] = " + arcLong);

        return arcLong;
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
                Double distance = Double.valueOf(Math.round(Math.sqrt(distanceX + distanceY)));

                //Se
                dist[i][j] = distance;
            }
        }

        return dist;
    }


    /**
     * Obtener nodo inicial desde un input.
     *
     */
    private void getInputInitialNode() {
        Scanner in = new Scanner(System.in);
        System.out.println("Ingresa el nodo inicial (del 1 al " + (nodes.size()) + "): ");
        String inputNode = in.nextLine();
        startNode = Integer.parseInt(inputNode) - 1;
        in.close();
    }

    /**
     * Se obtienen los datos desde un archivo de texto.
     *
     * @return nodes.
     */
    private List<Node> getDataFromFile() {
        return fileUtils.readFile();
    }

    /**
     * Muestra los resultados en pantalla.
     *
     * @param subtourK
     */
    private void showResults(List<Integer> subtourK) {
        System.out.println("\nOrden de las ciudades: ");
        int counter = 0;
        for (int nodeVisited : subtourK) {
            System.out.print(nodes.get(nodeVisited).getName());

            if(counter < subtourK.size()-1) {
                System.out.print(" -> ");
            }
            counter ++;
        }
        System.out.println("\n\nCosto total: " + Math.round(totalCost));
    }

    /**
     * Funcion para imprimir las distancias.
     *
     */
    private void printDistances() {
        System.out.println("Distancias: ");
        int size = (nodes.get(0).getName()).length();
        //Espacio inicial
        for (int i = 0; i < size+2 ; i++) {
            System.out.print(" ");
        }

        //Columnas
        for(int i=0; i<nodes.size(); i++) {
            System.out.print("\t" + nodes.get(i).getName() + "\t|");
        }
        System.out.println("");

        //Valores
        for(int i = 0; i < nodes.size(); i++) {
            System.out.print(nodes.get(i).getName() + " | ");
            for (int j=0; j < nodes.size(); j++) {
                System.out.print("\t" + distances[i][j] + "\t\t|");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
