package com.julian.tso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Double[][] distancias;

    private static List<Nodo> nodos;
    private static Double costoTotal;

    private static Integer nodoInicial;
    private static List<Integer> subtour;
    private static List<Integer> nodosRestantes;

    public static void main(String[] args) {
        nodos = new ArrayList<>();
        subtour = new ArrayList<>();
        nodosRestantes = new ArrayList<>();

	    resolver();
    }

    static void resolver() {
        File fichero = new File("ejercicio.txt");
        try {
            Scanner reader = new Scanner(fichero);
            while(reader.hasNext()) {
                // Leemos la siguiente linea en el archivo .txt
                String data = reader.nextLine();
                // Separamos los atributos por comas
                String[] attributes = data.split(",");

                // Creamos un objeto node para guardar los atributos
                Nodo node = new Nodo();
                node.setName(attributes[0]);
                node.setPositionX(Integer.parseInt(attributes[1].trim()));
                node.setPositionY(Integer.parseInt(attributes[2].trim()));

                //Agregamos el nodo a la lista
                nodos.add(node);
            }

        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        }

        // Se calculan las distancias entre nodos
        distancias = new Double[nodos.size()][nodos.size()];
        distancias = calcularDistancias();

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Cheapest Insertion Algorithm");
        System.out.println("----------------------------------------------------------------------------------------");

        // Se pide el nodo inicial
        Scanner in = new Scanner(System.in);
        System.out.println("introduce el nodo de inicio (del 1 al " + (nodos.size()) + "): ");
        String inputNode = in.nextLine();
        in.close();
        nodoInicial = Integer.parseInt(inputNode) - 1;

        subtour.add(nodoInicial); // Se guarda el nodo inicial.

        Double bestDistance = 100000000.0; // Distancia arbitrario default
        int mejorNodo = 0;
        // Se revisan los posibles nodos a visitar.
        for(int nextNode = 0; nextNode < nodos.size(); nextNode++) {
            if(!nodoYaVisitado(subtour, nextNode)) { // Se valida si el nodo ya ha sido visitado
                if (bestDistance > distancias[nodoInicial][nextNode]) {
                    bestDistance = distancias[nodoInicial][nextNode];
                    mejorNodo = nextNode;
                }
            }
        }

        subtour.add(mejorNodo);
        subtour.add(nodoInicial);

        System.out.println("----------------------------------------------------------------------------------------");
        // Se imprime subtour inicial
        System.out.println("Initial subtour: ");
        int counter = 0;
        for (int nodeVisited : subtour) {
            System.out.print(nodeVisited+1);

            if(counter < subtour.size()-1) {
                System.out.print(" -> ");
            }
            counter ++;
        }

        calcularCostoSubtour();
        System.out.println("\nCost: " + costoTotal);


        // Se obtienen los nodos que aun no se han visitado
        obtenerNodosNoVisitados();
        // Se calcula el proximo nodo a visitar
        while(!nodosRestantes.isEmpty()) {

            List<Integer> nextSubtour = new ArrayList<>();
            Double costNextSubtour = 1000000000000000000.0;
            int bestPos = 0;
            System.out.println("----------------------------------------------------------------------------------------");

            for(int k : nodosRestantes) { // Se recorren los nodos restantes

                int pos = 1; // Posible arco de insercion inicial

                // Se recorren los arcos
//                System.out.println("----------------------------------------------------------------------------------------");
                System.out.println("\nPossible subtours (next node: " + (k+1) + "): ");
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

                    // Se calcula el costo del nuevo arco
                    tmpCostSubtour = (distancias[tmpNextSubtour.get(pos-1)][tmpNextSubtour.get(pos)]
                            + distancias[tmpNextSubtour.get(pos)][tmpNextSubtour.get(pos+1)])
                            - distancias[tmpNextSubtour.get(pos-1)][tmpNextSubtour.get(pos+1)];

                    // Se imprime el proximo arco a insertar
                    System.out.println("Subtour: " + "(" + (tmpNextSubtour.get(pos-1)+1) + ", " + (tmpNextSubtour.get(pos)+1) + ")"
                            + " -> (" + (tmpNextSubtour.get(pos)+1) + ", " + (tmpNextSubtour.get(pos+1)+1) + ") = " + tmpCostSubtour);

                    if(costNextSubtour >= tmpCostSubtour) {
                        nextSubtour = tmpNextSubtour;
                        costNextSubtour = tmpCostSubtour;
                        bestPos = pos;
                    }

                    pos++;
                }
            }

            System.out.println("\nSubtour: " + "(" + (nextSubtour.get(bestPos-1)+1) + ", " + (nextSubtour.get(bestPos)+1) + ")"
                    + " -> " + "(" + (nextSubtour.get(bestPos)+1) + ", " + (nextSubtour.get(bestPos+1)+1) + ") = " + costNextSubtour + " <-- Selected");

            subtour = nextSubtour;
            obtenerNodosNoVisitados();

            calcularCostoSubtour();

            System.out.println("\nNext Subtour: ");
            counter = 0;
            for (int nodeVisited : subtour) {
                System.out.print(nodeVisited+1);

                if(counter < subtour.size()-1) {
                    System.out.print(" -> ");
                }
                counter ++;
            }

            System.out.println("\nCost: " + costoTotal);

            counter = 0;

            System.out.println("\nList of Nodes Remaining: ");
            if (!nodosRestantes.isEmpty()) {
                for(int nodoRestante : nodosRestantes) {

                    System.out.print(nodoRestante+1);

                    if(counter < nodosRestantes.size()-1) {
                        System.out.print(", ");
                    }
                    counter ++;
                }
                System.out.println("");
            }

        }

        // Se calcula el costo total.
        calcularCostoSubtour();

        // Se muestra el resultado
        mostrarResultados(subtour);
    }

    private static void calcularCostoSubtour() {
        costoTotal = 0.0;
        for(int i = 1; i < subtour.size(); i++) {
            costoTotal += distancias[subtour.get(i-1)][subtour.get(i)];
        }
    }

    private static boolean nodoYaVisitado(List<Integer> visitados, int proximoNodo) {
        boolean alreadyVisited = false;

        for(Integer nodeVisited : visitados) {
            if(nodeVisited == proximoNodo) {
                alreadyVisited = true;
                break;
            }
        }

        return alreadyVisited;
    }

    private static void obtenerNodosNoVisitados() {
        nodosRestantes = new ArrayList<>(); // Se limpia la lista al buscar los nodos restantes
        //Se recorren todos los nodos de la lista de nodos
        for(int nodeRev = 0; nodeRev < nodos.size(); nodeRev++) {

            //Se verifica el nodo en el subtour
            if (!nodoYaVisitado(subtour, nodeRev)) {
                nodosRestantes.add(nodeRev);
            }
        }
    }

    private static Double[][] calcularDistancias() {
        Double[][] dist = new Double[nodos.size()][nodos.size()];

        for (int i = 0; i < nodos.size(); i++) { //Nodo origen

            for (int j = 0; j < nodos.size(); j++) { //Node destino
                // Se obtienen coordenadas
                Double x1 = Double.valueOf(nodos.get(i).getPositionX());
                Double x2 = Double.valueOf(nodos.get(j).getPositionX());
                Double y1 = Double.valueOf(nodos.get(i).getPositionY());
                Double y2 = Double.valueOf(nodos.get(j).getPositionY());

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

    private static void mostrarResultados(List<Integer> subtourK) {
        System.out.println("\n----------------------------------------------------------------------------------------");
        System.out.println("Orden de las ciudades: ");
        int counter = 0;
        for (int nodeVisited : subtourK) {
            System.out.print(nodos.get(nodeVisited).getName());

            if(counter < subtourK.size()-1) {
                System.out.print(" -> ");
            }
            counter ++;
        }
        System.out.println("\n\nTotal Cost: " + Math.round(costoTotal));
    }

    static class Nodo {

        private String name;
        private Integer positionX;
        private Integer positionY;

        public Nodo() {

        }

        public Nodo(String name, Integer positionX, Integer positionY) {
            this.name = name;
            this.positionX = positionX;
            this.positionY = positionY;
        }

        public Integer getPositionX() {
            return positionX;
        }

        public void setPositionX(Integer positionX) {
            this.positionX = positionX;
        }

        public Integer getPositionY() {
            return positionY;
        }

        public void setPositionY(Integer positionY) {
            this.positionY = positionY;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
