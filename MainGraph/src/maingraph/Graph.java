/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maingraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Lam Tan Phat - CE181023
 */
public class Graph {

    int graph[][];
    static final int MAX_VERTEX = 100;
    int numberOfVertex;
    int numberOfVerties;
    ArrayList<Edge> edges = new ArrayList<>();
    int startTraversal;
    String resultDFS = "";
    String resultBFS = "";
    String resultIso = "";
    String resultEuler = "";
    String relsutFindVertex = "";
    String resultShortPaths = "";
    int countIsoVertex;
    int sizeGraphConnect;

    boolean isVisited[] = new boolean[MAX_VERTEX];
    int distance[] = new int[MAX_VERTEX];
    int theVertexBefore[] = new int[MAX_VERTEX];
    int start, end;
    boolean isConnected = true;

    int sumPrim = 0;
    int parentKruskal[] = new int[MAX_VERTEX];
    int sumKruskal = 0;
    ArrayList<Edge> listEdgeKruskal = new ArrayList<>();
    ArrayList<Edge> listEdgePrim = new ArrayList<>();
    ArrayList<Vertex> listCutVertices = new ArrayList<>();
    ArrayList<Edge> listCutEdges = new ArrayList<>();
    ArrayList<Integer> graphConnect = new ArrayList<>();
    ArrayList<String> graphResultSplit = new ArrayList<>();
    HashSet<Integer> graphHash = new HashSet<>();
    ArrayList<String> dis = new ArrayList<>();

    public Graph() {
        graph = new int[MAX_VERTEX][MAX_VERTEX];
        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                graph[i][j] = 0;
            }
        }
    }

    public void readDataMatrix(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            numberOfVertex = sc.nextInt();
//            start = sc.nextInt();
//            end = sc.nextInt();
            for (int i = 0; i < numberOfVertex; i++) {
                for (int j = 0; j < numberOfVertex; j++) {
                    graph[i][j] = sc.nextInt();
                    if (graph[i][j] > 0 && i < j) {
                        edges.add(new Edge(new Vertex(i), new Vertex(j), graph[i][j]));
                    }
                }
            }
        } catch (FileNotFoundException e) {
        }
    }

    public void readDataList(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            numberOfVertex = sc.nextInt();
            int numEdge = sc.nextInt();
            startTraversal = sc.nextInt();
            for (int i = 0; i < numEdge; i++) {
                int start = sc.nextInt();
                int end = sc.nextInt();
                graph[start][end] = graph[end][start] = 1;
                edges.add(new Edge(new Vertex(start), new Vertex(end), 1));
            }
        } catch (FileNotFoundException e) {
        }
    }

    public void resetIsVisited() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            isVisited[i] = false;
        }
    }

    public void DFS(int start) {
//        resetIsVisited();
        Stack<Integer> s = new Stack();
        s.push(start);
        int fromVertex;
        while (!s.isEmpty()) {
            fromVertex = s.pop();
            if (isVisited[fromVertex] == false) {
                resultDFS += "," + fromVertex;
                isVisited[fromVertex] = true;
                for (int toVertex = numberOfVertex - 1; toVertex >= 0; toVertex--) {
                    if (graph[fromVertex][toVertex] > 0 && isVisited[toVertex] == false) {
                        s.push(toVertex);
                    }
                }
            }
        }
    }

    public void BFS(int start) {
        resetIsVisited();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        int fromVertex;
        while (!queue.isEmpty()) {
            fromVertex = queue.poll();
            if (isVisited[fromVertex] == false) {
                resultBFS += "," + fromVertex;
                isVisited[fromVertex] = true;
                for (int toVertex = 0; toVertex < numberOfVertex; toVertex++) {
                    if (graph[fromVertex][toVertex] > 0 && isVisited[toVertex] == false) {
                        queue.offer(toVertex);
                    }
                }
            }
        }
    }

    /**
     * Isolated vertices counting
     */
    public void isoVertex() {
        countIsoVertex = 0;
        for (int i = 0; i < numberOfVertex; i++) {
            int degree = 0;
            for (int j = 0; j < numberOfVertex; j++) {
                degree += graph[i][j];
            }
            if (degree == 0) {
                resultIso += "," + i;
                countIsoVertex++;
            }
        }
        if (countIsoVertex == 0) {
            resultIso += "There is a connected graph";
        }
    }

    public void resetDistance() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
    }

    public void resetTheVertexBefore() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            theVertexBefore[i] = i;
        }
    }

    public void dijkstra() {
        resetIsVisited();
        resetDistance();
        resetTheVertexBefore();
        distance[start] = 0;
        int current;
        for (int i = 0; i < numberOfVertex; i++) {
            current = findNearestVertex();
            if (current == -1) {
                isConnected = false;
                break;
            } else {
                isVisited[current] = true;
                for (int toVerTex = 0; toVerTex < numberOfVertex; toVerTex++) {
                    if (graph[current][toVerTex] > 0 && isVisited[toVerTex] == false
                            && distance[toVerTex] > distance[current] + graph[current][toVerTex]) {
                        distance[toVerTex] = distance[current] + graph[current][toVerTex];
                        theVertexBefore[toVerTex] = current;
                    }
                }
            }
        }
    }

    /**
     * Shortest Path
     *
     * @return
     */
    public String printDijkstra() {
        dijkstra();
        String path = "" + end;
        int current = end;
        while (current != start) {
            current = theVertexBefore[current];
            path = current + "->" + path;
        }
        path += ": " + distance[end];

        return path;
    }

    /**
     * resultShortPaths
     *
     * @return
     */
    public String printShortestPath() {
        dijkstra();
        String path = "";
        for (int end = 0; end < numberOfVertex; end++) {
            path = "" + end;
            int current = end;
            while (current != start) {
                current = theVertexBefore[current];
                path = current + "->" + path;
            }
            path += ": " + distance[end];
            resultShortPaths += path + "\r\n";

        }
        return resultShortPaths.trim();
    }

    private int findNearestVertex() {
        int minIndex = -1;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < numberOfVertex; i++) {
            if (isVisited[i] == false && distance[i] < minValue) {
                if (distance[i] < minValue) {
                    minValue = distance[i];
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    public void prim() {
        resetDistance();
        resetIsVisited();
        resetTheVertexBefore();
        distance[0] = 0;
        int current;
        for (int i = 0; i < numberOfVertex; i++) {
            current = findNearestVertex();
            if (current == -1) {
                isConnected = false;
                break;
            } else {
                sumPrim += distance[current];
                isVisited[current] = true;
                for (int toVertex = 0; toVertex < numberOfVertex; toVertex++) {
                    if (graph[current][toVertex] > 0 && isVisited[toVertex] == false
                            && distance[toVertex] > graph[current][toVertex]) {
                        distance[toVertex] = graph[current][toVertex];
                        theVertexBefore[toVertex] = current;
                    }
                }
            }
        }
    }

    public void resetParentKruskal() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            parentKruskal[i] = i;
        }
    }

    public int findParent(int v) {
        if (v == parentKruskal[v]) {
            return v;
        }
        return findParent(parentKruskal[v]);
    }

    public boolean union(int a, int b) {
        int a1 = findParent(a);
        int b1 = findParent(b);
        if (a1 == b1) {
            return false;
        }
        parentKruskal[b] = a1;
        return true;
    }

    public void kruskal() {
        resetParentKruskal();
        Collections.sort(edges);
        for (Edge e : edges) {
            if (union(e.getStart().value, e.getEnd().value)) {
                sumKruskal += e.getWeight();
                listEdgeKruskal.add(e);
            }
        }
    }

    /**
     * Check the graph has Euler or not
     *
     * @return
     */
    public boolean checkEuler() {
        boolean checkEuler = false;
        int countCheckEuler = 0;
        for (int i = 0; i < numberOfVertex; i++) {
            int sumVer = 0;
            for (int j = 0; j < numberOfVertex; j++) {
                sumVer += graph[i][j];
            }
            if (sumVer % 2 == 0 && sumVer != 0) {
                countCheckEuler++;
            }
        }
        if (countCheckEuler == numberOfVertex) {
            checkEuler = true;
        } else {
            checkEuler = false;
        }
        return checkEuler;
    }

    /**
     * Find cycle of Euler
     *
     * @param start
     */
    public void euler(int start) {
        if (checkEuler()) {
            String s = "";
            resetIsVisited();
            resetDistance();
            resetTheVertexBefore();
            distance[start] = 0;
            Stack<Integer> euler = new Stack<>();
            euler.push(start);
            while (!euler.isEmpty()) {
                int current = euler.peek();
                boolean noVisited = false;
                for (int i = 0; i < numberOfVertex; i++) {
                    if (graph[current][i] > 0 && !isVisited[i]) {
                        euler.push(i);
                        graph[current][i] = graph[i][current] = 0;
                        noVisited = true;
                        break;
                    }
                }
                if (!noVisited) {
                    int top = euler.pop();
                    s += "," + top;
                    if (euler.size() == 0 && checkEuler()) {
                        euler.push(top);
                    }
                }
            }
            resultEuler = s.substring(1);
        } else {
            resultEuler += "No eulerian cycle!";
        }
    }

    /**
     * Method to find vertex form start
     *
     * @param start
     */
    public void findVertex(int start) {
        relsutFindVertex = "";
        resetIsVisited();
        Stack<Integer> s = new Stack<>();
        s.push(0);
        int fromVertex;
        while (!s.isEmpty()) {
            fromVertex = s.pop();
            if (isVisited[fromVertex] == false) {
                isVisited[fromVertex] = true;
                if (fromVertex >= start) {
                    relsutFindVertex += "," + fromVertex;
                }
                for (int toVertex = numberOfVertex - 1; toVertex >= 0; toVertex--) {
                    if (graph[fromVertex][toVertex] > 0 && isVisited[toVertex] == false) {
                        s.push(toVertex);
                    }
                }
            }
        }
    }

    public void findCutVertex() {
        int theNumberOfConnectedComponent = 0;
        for (int i = 0; i < numberOfVertex; i++) {
            if (isVisited[i] == false) {
                theNumberOfConnectedComponent++;
                DFS(i);
            }
        }
        for (int i = 0; i < numberOfVertex; i++) {
            resetIsVisited();
            isVisited[i] = true;
            int count = 0;
            for (int j = 0; j < numberOfVertex; j++) {
                if (isVisited[j] == false) {
                    count++;
                    DFS(j);
                }
                if (count > theNumberOfConnectedComponent) {
                    listCutVertices.add(new Vertex(i));
                }
            }
        }
    }

    public void DFS2(int start, int x, int y) {
        resultDFS = "";
        resetIsVisited();
        Stack<Integer> s = new Stack<>();
        s.push(start);
        int fromVertex;
        while (!s.isEmpty()) {
            fromVertex = s.pop();
            if (isVisited[fromVertex] == false) {
                resultDFS += "," + fromVertex;
                isVisited[fromVertex] = true;
                for (int toVertex = numberOfVertex - 1; toVertex >= 0; toVertex--) {
                    if (graph[fromVertex][toVertex] > 0 && isVisited[toVertex] == false) {
                        if (fromVertex == x && toVertex == y) {
                            continue;
                        }
                        s.push(toVertex);
                    }
                }
            }
        }
    }

    public void findCutEdge() {
        int theNumberOfConnectedComponent = 0;
        for (int i = 0; i < numberOfVertex; i++) {
            if (isVisited[i] == false) {
                theNumberOfConnectedComponent++;
                DFS(i);
            }
        }
        System.out.println("theNumberOfConnectedComponent: " + theNumberOfConnectedComponent);
        for (Edge edge : edges) {
            int x = edge.start.value;
            int y = edge.end.value;
            resetIsVisited();
            int count = 0;
            for (int j = 0; j < numberOfVertex; j++) {
                if (isVisited[j] == false) {
                    count++;
                    DFS2(j, x, y);
                    System.out.println("j: " + j + " - count: " + count);
                    if (count > theNumberOfConnectedComponent) {
                        listCutEdges.add(edge);
                    }
                }
            }
        }
    }

    public void connectedGraph() {
        BFS(0);
        for (int i = 1; i < resultBFS.length(); i += 2) {
            graphConnect.add(Integer.parseInt(resultBFS.substring(i, i + 1)));
        }
        for (int i = 0; i < numberOfVertex; i++) {
            for (int j = 0; j < numberOfVertex; j++) {
                if (!graphConnect.contains(i) && !graphConnect.contains(j)) {
                    graphHash.add(i);
                }
            }
        }
        graphResultSplit.add(resultBFS.substring(1));
        int count = 0;
        ArrayList<Integer> listGraphHash = new ArrayList<>(graphHash);
        for (Integer iso : listGraphHash) {
            if (!graphConnect.contains(iso)) {
                resultBFS = "";
                BFS(iso);
                for (int i = 1; i < resultBFS.length(); i += 2) {
                    graphConnect.add(Integer.parseInt(resultBFS.substring(i, i + 1)));
                }
                for (int i = 0; i < numberOfVertex; i++) {
                    for (int j = 0; j < numberOfVertex; j++) {
                        if (!graphConnect.contains(i) && !graphConnect.contains(j)) {
                            graphHash.add(i);
                        }
                    }
                }
                graphResultSplit.add(resultBFS.substring(1));
            }
        }
        sizeGraphConnect = graphResultSplit.size();
    }

    public void showGraph() {
        for (int i = 0; i < numberOfVertex; i++) {
            for (int j = 0; j < numberOfVertex; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void prinData(String fileName) {
        findCutVertex();
        try {
            String s ="";
            FileWriter out = new FileWriter(fileName);
            out.write(listCutVertices.size()+"\n");
            for (Vertex sn : listCutVertices) {
                s+=","+sn.value;
            }
            out.write(s.substring(1));
            out.close();
        } catch (IOException e) {
        }
    }

}
