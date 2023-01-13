package org.example.lab7;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab4.MatrixService;
import org.example.lab6.BorderConditionService;

import java.util.List;

@Deprecated
public class MatrixC {

    private static double[][] globalAggregationMatrixC = new double[16][16];

    public static double[][] calculateMatrixC(Grid grid, int numberOfNodes) {
        int length = numberOfNodes * numberOfNodes;
        int density = GlobalData.density; // gestosc
        int specificHeat = GlobalData.specificHeat; // ciepło wlasciwe

        // wspolrzedne punktow calkowania
//        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
//        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
//        double[] weight = new double[]{1.0, 1.0};
        double[] ksi = GlobalData.getKsiCoordinates(numberOfNodes);
        double[] eta = GlobalData.getEtaCoordinates(numberOfNodes);
        List<Node> nodes = grid.getNodes();
        List<Element> elements = grid.getElements();

        // Wartości dla funkcji kształtu // Values for geometric models         //{N}*{N}^T
        double[][] geometricModelsValues = new double[4][4];
        for (int i = 0; i < 4; i++) { // funkcje kształtów
            for (int j = 0; j < 4; j++) {
                geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
            }
        }
        System.out.println("geometricModelsValues: ");
        MatrixService.showTable2D(geometricModelsValues);


        // for every element !!!
        for (int element = 0; element < grid.getElementsNumber(); element++) {
            Element element1 = elements.get(element);
            double[] detJWallElement1 = BorderConditionService.calculateDetJForElement(nodes, element1);
            System.out.println("detJ for element " + element);
            MatrixService.showTable1D(detJWallElement1);


            double[][][] matrixCForFourPoints = new double[length][4][4]; // there will be 9 matrix C for every point if numberOfNodes = 3
            for (int pc = 0; pc < 4; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        matrixCForFourPoints[pc][i][j] =
                                geometricModelsValues[pc][i] * geometricModelsValues[pc][j];
                    }
                }
                System.out.println("matrixCForFourPoints " + pc);
                MatrixService.showTable2Dshort(matrixCForFourPoints[pc]);
            }

            double[][] resultMatrixC = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    resultMatrixC[i][j] = specificHeat * density * 2.7777776463888833E-4 * matrixCForFourPoints[0][i][j] // sum for one element
                            + specificHeat * density * 2.7777776463888833E-4 * matrixCForFourPoints[1][i][j]
                            + specificHeat * density * 2.7777776463888833E-4 * matrixCForFourPoints[2][i][j]
                            + specificHeat * density * 2.7777776463888833E-4 * matrixCForFourPoints[3][i][j];
                }
            }
            System.out.println("resultMatrixC: ");
            MatrixService.showTable2D(resultMatrixC); // for one element

            List<Integer> e1IDs = element1.getIDs();
            // here is the aggregation
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationMatrixC[e1IDs.get(i) - 1][e1IDs.get(j) - 1] += resultMatrixC[i][j]; // without + !!! just = ???
                }
            }


            // dla jednego wezla jest jedna macierz C
            // wiec wychodza 4 macierze
            // trzeba jj zsumowac
        }

        return globalAggregationMatrixC;
    }

}
