package org.example.deprecated.lab5;

import org.example.model.Element;
import org.example.model.Grid;

import java.util.List;
@Deprecated
public class Aggregation {

    public static double[][] calculateAggregation(Grid grid, double[][] matrixHForTwoPointIntegration1) {
        List<Element> elements = grid.getElements();
        double[][] aggregation = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        for (int e = 0; e < elements.size(); e++) {
            Element element = elements.get(e);
            for (int i = 0; i < 4; i++) {
//                System.out.print(element.getIDs().get(i) + ", ");
                for (int j = 0; j < 4; j++) {
                    aggregation[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += matrixHForTwoPointIntegration1[i][j];
                }
//                System.out.println();
            }
        }
        return aggregation;
    }
}
