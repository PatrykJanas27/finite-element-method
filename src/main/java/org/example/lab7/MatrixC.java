package org.example.lab7;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab4.MatrixService;
import org.example.lab6.BorderConditionService;

import java.util.List;

import static java.lang.Math.sqrt;

public class MatrixC {

    private static double[][] globalAggregationMatrixC = new double[16][16];

    public static double[][] calculateMatrixC(Grid grid, GlobalData globalData) {
        int simulationStepTime = globalData.getSimulationStepTime(); // "tau"
        int initialTemp = globalData.getInitialTemp();
        int density = globalData.getDensity(); // gestosc
        int specificHeat = globalData.getSpecificHeat(); // ciepło wlasciwe

        // wspolrzedne punktow calkowania
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
        double[] weight = new double[]{1.0, 1.0};
        List<Node> nodes = grid.getNodes();
        List<Element> elements = grid.getElements();
        int elementMax = 9;
        for (int element = 0; element < elementMax; element++) {
            Element element1 = elements.get(element);
            double[] detJWallElement1 = BorderConditionService.calculateDetJForElement(nodes, element1);
            System.out.println("detJ for element " + element);
            MatrixService.showTable1D(detJWallElement1);
            //{N}*{N}^T
            // Wartości dla funkcji kształtu // Values for geometric models
            double[][] geometricModelsValues = new double[4][4];
            for (int i = 0; i < 4; i++) { // funkcje kształtów
                for (int j = 0; j < 4; j++) {
                    geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
                    geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
                    geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
                    geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
                }
            }
            System.out.println("geometricModelsValues: ");
            MatrixService.showTable2D(geometricModelsValues);

            double[][][] matrixCForFourPoints = new double[4][4][4]; // there will be 4 matrix C for every point
            for (int point = 0; point < 4; point++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        matrixCForFourPoints[point][i][j] = specificHeat * density *
                                geometricModelsValues[point][i] * geometricModelsValues[point][j] *
                                detJWallElement1[point] * weight[0] * weight[1];
                    }
                }
                System.out.println("matrixCForFourPoints " + point);
                MatrixService.showTable2Dshort(matrixCForFourPoints[point]);
            }

            double[][] resultMatrixC = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    resultMatrixC[i][j] = matrixCForFourPoints[0][i][j] // sum for one element
                            + matrixCForFourPoints[1][i][j]
                            + matrixCForFourPoints[2][i][j]
                            + matrixCForFourPoints[3][i][j];
                }
            }
            System.out.println("resultMatrixC: ");
            MatrixService.showTable2D(resultMatrixC); // for one element
            List<Integer> e1IDs = elements.get(elementMax - 1 - element).getIDs();
            e1IDs = element1.getIDs();
            // here is the aggregation
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationMatrixC[e1IDs.get(i) - 1][e1IDs.get(j) - 1] = resultMatrixC[i][j]; // without + !!! just = ???
                }
            }


            // dla jednego wezla jest jedna macierz C
            // wiec wychodza 4 macierze
            // trzeba jj zsumowac
        }

        return globalAggregationMatrixC;
    }

}
