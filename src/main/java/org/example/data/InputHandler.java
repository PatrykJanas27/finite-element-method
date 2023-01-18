package org.example.data;

import lombok.CustomLog;
import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//@Slf4j(topic = "InputHandler")
@CustomLog(topic = "InputHandler")
public class InputHandler {
    public static Grid readFile(String fileName) throws FileNotFoundException {
        log.info("Reading file: " + fileName);
        Grid grid = new Grid();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                readGlobalDataFromFile(line);
                readGridDataFromFile(grid, line);
                readNodeCoordinatesDataFromFile(grid, scanner, line);
                readElementDataFromFile(grid, scanner, line);
                readBcDataFromFile(grid, scanner, line);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found! " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad data format, can not parse string");
        }
        System.out.println(grid);
        return grid;
    }

    private static void readBcDataFromFile(Grid grid, Scanner scanner, String line) {
        if (line.equals("*BC")) {
            String[] BCs = scanner.nextLine().split(",");
            List<Node> nd = grid.getNodes();
            for (String bc : BCs) {
                int BCsNodeNumber = Integer.parseInt(bc.trim());
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
            grid.setElements(elements);
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
            grid.setNodes(nodes);
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

    private static void readGlobalDataFromFile(String line) {
        if (line.contains("SimulationTime")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.simulationTime = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("SimulationStepTime")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.simulationStepTime = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("Conductivity")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.conductivity = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("Alfa")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.alfa = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("Tot")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.tot = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("InitialTemp")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.initialTemp = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("Density")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.density = Integer.parseInt(s[1]);
            }
        }
        if (line.contains("SpecificHeat")) {
            String[] s = line.split(" ");
            if (s.length == 2) {
                GlobalData.specificHeat = Integer.parseInt(s[1]);
            }
        }
    }
}
