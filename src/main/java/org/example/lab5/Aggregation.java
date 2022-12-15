package org.example.lab5;

import org.example.lab1.Element;
import org.example.lab1.Grid;

import java.util.List;

public class Aggregation {

    public static double[][] calculateAggregation(Grid grid, List<Element> elements, double[][] matrixHForTwoPointIntegration1) {
        double[][] aggregation = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        for (int e = 0; e < elements.size(); e++) {
            Element element = elements.get(e);
            for (int i = 0; i < 4; i++) {
                System.out.print(element.getID().get(i) + ", ");
                for (int j = 0; j < 4; j++) {
                    aggregation[element.getID().get(i) - 1][element.getID().get(j) - 1] += matrixHForTwoPointIntegration1[i][j];
                }
                System.out.println();
            }
        }
        return aggregation;
    }
}
