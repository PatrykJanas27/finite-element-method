package org.example.lab1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String FILE_NAME1 = "src/main/resources/lab1/Test1_4_4.txt";
    private static final String FILE_NAME2 = "src/main/resources/lab1/Test2_4_4_MixGrid.txt";
    private static final String FILE_NAME3 = "src/main/resources/lab1/Test3_31_31_kwadrat.txt";

    public static void main(String[] args) throws FileNotFoundException, NumberFormatException {
        readFile(FILE_NAME1);
    }

    private static void readFile(String fileName) throws FileNotFoundException {
        GlobalData globalData = new GlobalData();
        Grid grid = new Grid();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                readGlobalDataFromFile(globalData, line);
                readGridDataFromFile(grid, line);
                readNodeCoordinatesDataFromFile(grid, scanner, line);
                readElementDataFromFile(grid, scanner, line);
                readBcDataFromFile(grid, scanner, line);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found! " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad data format, can not parse string to integer/double");
        }

        System.out.println(globalData);
        System.out.println(grid);
    }

    private static void readBcDataFromFile(Grid grid, Scanner scanner, String line) {
        if (line.equals("*BC")) {
            String[] BCs = scanner.nextLine().split(",");
            List<Node> nd = grid.getND();
            for (int i = 0; i < BCs.length; i++) {
                int BCsNodeNumber = Integer.parseInt(BCs[i].trim());
                nd.get(BCsNodeNumber - 1).setBC(true);
            }
        }
    }

    private static void readElementDataFromFile(Grid grid, Scanner scanner, String line) {
        if (line.contains("*Element")) {
            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < grid.getElementsNumber(); i++) {
                line = scanner.nextLine();
                String[] nextElementCoordinates = line.split(",");
                List<Integer> elementNumbers = new ArrayList<>();
                int e1 = Integer.parseInt(nextElementCoordinates[1].trim());
                int e2 = Integer.parseInt(nextElementCoordinates[2].trim());
                int e3 = Integer.parseInt(nextElementCoordinates[3].trim());
                int e4 = Integer.parseInt(nextElementCoordinates[4].trim());
                elementNumbers.add(e1);
                elementNumbers.add(e2);
                elementNumbers.add(e3);
                elementNumbers.add(e4);
                elements.add(new Element(elementNumbers));
            }
            grid.setEL(elements);
        }
    }

    private static void readNodeCoordinatesDataFromFile(Grid grid, Scanner scanner, String line) {
        if (line.contains("*Node")) {
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < grid.getNodesNumber(); i++) {
                line = scanner.nextLine();
                String[] nextNode = line.split(",");
                nodes.add(new Node(Double.parseDouble(nextNode[1]), Double.parseDouble(nextNode[2])));
            }
            grid.setND(nodes);
        }
    }

    private static void readGridDataFromFile(Grid grid, String line) {
        if (line.contains("Nodes number")) {
            String[] s = line.split(" ");
            if (s.length == 3) {
                grid.setNodesNumber(Integer.parseInt(s[2]));
            }
        }
        if (line.contains("Elements number")) {
            String[] s = line.split(" ");
            if (s.length == 3) {
                grid.setElementsNumber(Integer.parseInt(s[2]));
            }
        }
    }

    private static void readGlobalDataFromFile(GlobalData globalData, String line) {
        if (line.contains("SimulationTime")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setSimulationTime(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("SimulationStepTime")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setSimulationStepTime(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("Conductivity")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setConductivity(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("Alfa")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setAlfa(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("Tot")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setTot(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("InitialTemp")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setInitialTemp(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("Density")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setDensity(Integer.parseInt(s[1]));
            }
        }
        if (line.contains("SpecificHeat")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                globalData.setSpecificHeat(Integer.parseInt(s[1]));
            }
        }
    }
}
