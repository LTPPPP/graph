/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maingraph;

/**
 *
 * @author Lam Tan Phat - CE181023
 */
public class MainGraph {

    Graph obj = new Graph();
    String inputFile = "input.txt";
    String outputFile = "output.txt";
    String fi, fo;

    void setFile(String[] args) {
        fi = args.length >= 2 ? args[0] : inputFile;
        fo = args.length >= 2 ? args[1] : outputFile;
    }

    void readFile() {
        // If input is matrix
         obj.readDataMatrix(fi);
        // if input is list
        // obj.readDataList(fi);
    }

    void writeData() {
        obj.prinData(fo);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic herese
        MainGraph objs = new MainGraph();
        objs.setFile(args);
        objs.readFile();
        objs.writeData();
    }
}
