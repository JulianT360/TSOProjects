package com.tso.jatd.algorithms;

import com.tso.jatd.FileUtils;
import com.tso.jatd.Node;

import java.util.ArrayList;
import java.util.List;

public class NNAlgorithm {

    private FileUtils fileUtils;
    private Double[][] distances;
    private List<Node> nodes;
    private int startNode;

    public NNAlgorithm() {
        fileUtils = new FileUtils();
        nodes = new ArrayList<>();
        startNode = 1;
    }

    public void solve() {
        nodes = fileUtils.readFile();
        distances = new Double[nodes.size()][nodes.size()];
        distances = calculateEucladianDistances();

        System.out.println(distances.toString());

        int[] nodesVisited = new int[nodes.size()+1];

        calculateNodesVisited();
    }

    private int[] calculateNodesVisited() {
        int[] visited = new int[nodes.size()+1];
        //Se guarda el nodo inicial (elegido por el usuario)
        visited[0] = startNode;

        Double bestDistance = 0.0;
        int bestNode = 0;

        for (int i=0; i<nodes.size();i++) {

            for (int j=0; j<nodes.size();j++) {
                if (visited[i] != j) {
                    bestDistance = bestDistance <= distances[i][j] ? bestDistance : distances[i][j];
                }
            }

        }
        return visited;
    }

    private Double[][] calculateEucladianDistances () {
        Double[][] dist = new Double[nodes.size()][nodes.size()];

        for(int i=0; i<nodes.size(); i++) { //Nodo origen

            for(int j=0; j < nodes.size(); j++) { //Node destino
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
