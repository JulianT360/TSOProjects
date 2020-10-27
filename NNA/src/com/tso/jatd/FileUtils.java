package com.tso.jatd;

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
        File fichero = new File("data.txt");
        try {
            Scanner reader = new Scanner(fichero);
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
