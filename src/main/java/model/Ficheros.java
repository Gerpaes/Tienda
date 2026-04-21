/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Estudio-Trabajo
 */
public class Ficheros {

    File myObj = new File("inputInventory.txt");
    
    private static void preguntasFichero(File file, File file2){
        System.out.println("¿ " + file + " es un fichero? " + file.isFile() );
        System.out.println("¿" + file + " es un directorio? " + file.isDirectory());
        System.out.println("¿Cual es la ruta absoluta del fichero? " + file);
        System.out.println("¿Cual es la ruta absoluta del fichero? " + file.getAbsolutePath());
        System.out.println("¿Cual es el nombre del fichero? " + file.getName());
        System.out.println("¿En que directorio se encuentra el fichero? " + file.getParent());
        System.out.println("¿Cual es el tamaño del fichero en bytes? " + file.length());
        System.out.println("¿Cuando se modificó por última vez en ms? " + file.lastModified());
        System.out.println("¿Tenemos permisos para leer el fichero? " + file.canRead());
        System.out.println("¿Tenemos permisos para escribir en el fichero? " + file.canWrite());
        
        for(String aux :file2.list()){
            System.out.println(aux);
        }
        for(File aux : file2.listFiles()){
            System.out.println(aux.getName());
        }   
    }

    public static void EscrituraFichero(HashMap<Integer,Product> inventory) {
        FileWriter outputStreamProduct = null ;
        BufferedWriter out1 = null;
        

        try {
            outputStreamProduct = new FileWriter("inputInventory.txt");
            out1 = new BufferedWriter(outputStreamProduct);
            Iterator it = inventory.keySet().iterator();
            while(it.hasNext()){
                Product key = (Product)it.next();

                out1.write("Product" + ":" + key.getName() + ";" + "Wholesaler Price" + ":" + key.getWholesalerPrice().getValue() + ";" + "Stock" + ":" + key.getStock() + "\n");
            }

        } catch (java.io.FileNotFoundException ex) {
            System.out.println("No existe el archivo para ser leido.");
        } catch (java.io.IOException ex) {

        } finally {
            if (out1 != null) {
                try {
                    out1.close();
                } catch (IOException ex) {
                    System.out.println("No se puede acceder al archivo.");
                }
            }
        }
    }

     public static void writeSales(ArrayList<Sale> sales) {
        FileWriter outputStreamProduct = null ;
        BufferedWriter out1 = null;
        
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateFormatted = date.format(formatter);

        String fileName = "sales_" + dateFormatted + ".txt";

        try {
            outputStreamProduct = new FileWriter(fileName);
            out1 = new BufferedWriter(outputStreamProduct);
            int index = 1;
            for (Sale sale : sales) {

                out1.write(index + " Client" + "=" + sale.getClient().getName() + ";" + "\n" + "Products" + " = " + sale.getProducts().toString() + ";" + "\n" + "Amount " + "= " + sale.getAmount() +  "; " + "\n");
                index++;
            }

        } catch (java.io.FileNotFoundException ex) {
            System.out.println("No existe el archivo para ser leido.");
        } catch (java.io.IOException ex) {

        } finally {
            if (out1 != null) {
                try {
                    out1.close();
                } catch (IOException ex) {
                    System.out.println("No se puede acceder al archivo.");
                }
            }
        }
    }
    
    public static void LecturaFichero(HashMap<Integer,Product> inventory) {

        String linea;

        String nombre = null;

        Amount wholesalerprice = new Amount(0.0);

        int stock = 0;

        FileReader inputStream = null;
        BufferedReader in = null;

        try {
            inputStream = new FileReader("inputInventory.txt");
            in = new BufferedReader(inputStream);

            while ((linea = in.readLine()) != null) {

                String datos[] = linea.split(";");

                for (int i = 0; i < datos.length; i ++) {
                    String finalDatos[] = datos[i].split(":");
                    if (finalDatos[0].equals("Product")) {
                        nombre = finalDatos[1];
                    } else if (finalDatos[0].equals("Wholesaler Price")) {
                        wholesalerprice = new Amount(Double.parseDouble(finalDatos[1]));
                    } else if (finalDatos[0].equals("Stock")) {
                        stock = Integer.parseInt(finalDatos[1]);

                    }

                }
                Product p = new Product(nombre, wholesalerprice, true, stock);
               inventory.put(3,p);
                
            }

        } catch (java.io.IOException ex) {
            System.out.println(ex);
            System.out.println("No se puede acceder al archivo.");
            
        }  finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    System.out.println("No se puede acceder al archivo.");
                }
            }
        }

    }
    
    public static boolean findproduct(String nombre){
        String linea;
        FileReader inputStream = null;
        BufferedReader in = null;
        
        try {
            inputStream = new FileReader("inputInventory.txt");
            in = new BufferedReader(inputStream);
            
            while((linea = in.readLine()) != null){
                
                String datos[] = linea.split(";");
                
                for(int i = 0; i < datos.length; i++){
                    
                    String finalDatos[] = datos[i].split(":");
                    
                    if(finalDatos[0].equals("Product")){
                        if(finalDatos[1].equalsIgnoreCase(nombre)){
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("No se puede acceder al archivo.");
        } finally {
            
            if(in != null){
                try{
                    in.close();
                } catch(IOException ex){
                    System.out.println("No se puede cerrar el archivo.");
                }
            }
        }
        return false;
    }

}
