package com.fime.tso.util;

import com.fime.tso.objects.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

    /**
     * Metodo para leer un archivo.
     *
     * @return Lista de nodos.
     */
    public List<Node> readFile() {
        List<Node> listNodes = new ArrayList<>();
        File fichero = new File("instance/VRPNC1m.txt");
        try {
            Scanner reader = new Scanner(fichero);

            String dataCl = reader.nextLine();
            // Se guarda el numero de cliente
            String clients = dataCl.split(":")[1].trim();

            // Se guarda el numero de viajes
            String dataTrips = reader.nextLine();
            String trips = dataTrips.split(":")[1].trim();

            // Se guarda la capacidad de vehiculos
            String dataCapacity = reader.nextLine();
            String vCapacity = dataCapacity.split(":")[1].trim();

            // Se guardan las demandas de los clientes
            String dataDemands = reader.nextLine();
            String[] clientsDemands = dataDemands.split(",");

            // 

            while(reader.hasNext()) {
                // Leemos la siguiente linea en el archivo .txt
                String data = reader.nextLine();
                // Separamos los atributos por comas
                String[] attributes = data.split(",");

                // Creamos un objeto node para guardar los atributos
                Node node = new Node();
                node.setName(attributes[0]);
                node.setPositionX(Integer.parseInt(attributes[1].trim()));
                node.setPositionY(Integer.parseInt(attributes[2].trim()));

                //Agregamos el nodo a la lista
                listNodes.add(node);
            }

        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        }

        return listNodes;
    }
}
