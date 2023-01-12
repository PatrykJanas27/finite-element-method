package org.example.lab4;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab6.BorderConditionService;

import java.util.List;
import java.util.function.Function;

public class MatrixHService {

    public static double[][] globalAggregationH;
    public static double[][] globalAggregationMatrixC;

    public static void calculate_MatrixH_and_MatrixC(Grid grid, int numberOfNodes) {
        if (numberOfNodes < 2 || numberOfNodes > 5) {
            throw new IllegalArgumentException("There is no method to calculate matrix with number fo nodes = " + numberOfNodes);
        }
        int density = GlobalData.density; // gestosc
        int specificHeat = GlobalData.specificHeat; // ciepło wlasciwe
        globalAggregationH = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        globalAggregationMatrixC = new double[grid.getNodesNumber()][grid.getNodesNumber()];


        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();
        int length = numberOfNodes * numberOfNodes;
        int elementsNumber = grid.getElementsNumber();
        double[][][] Hpcs = new double[length][4][4];
        double[] weightsOfPoints = GlobalData.getWeightsArray(numberOfNodes); //{1.0, 1.0}
        // --> main loop for elements
        for (int e = 0; e < grid.getElements().size(); e++) {
            Element element = elements.get(e);
            double[] pcx = new double[4];
            double[] pcy = new double[4];
            for (int i = 0; i < nodes.size(); i++) { // < 4
                for (int j = 0; j < 4; j++) { // 4 wspolrzedne x i y
                    pcx[j] = nodes.get(element.getIDs().get(j) - 1).getX();
                    pcy[j] = nodes.get(element.getIDs().get(j) - 1).getY();
                }
            }
            double[] ksi = GlobalData.getKsiCoordinates(numberOfNodes);
            double[] eta = GlobalData.getEtaCoordinates(numberOfNodes);
            // geometrix models for matrix C **************** (ksi, eta) -> 0.25 * (1 - ksi) * (1 - eta))
            // Wartości dla funkcji kształtu // Values for geometric models         //{N}*{N}^T
            double[][] geometricModelsValues = new double[4][4];
            for (int i = 0; i < 4; i++) { // funkcje kształtów
                for (int j = 0; j < 4; j++) {
                    geometricModelsValues[i][j] = BorderConditionService.geometricModelsN(j, ksi[i], eta[i]);
                }
            }
//            System.out.println("geometricModelsValues: ");
//            MatrixService.showTable2D(geometricModelsValues);
            //
            double[][][] matrixCForFourPoints = new double[length][4][4]; // there will be 9 matrix C for every point if numberOfNodes = 3
            for (int pc = 0; pc < 4; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        matrixCForFourPoints[pc][i][j] =
                                geometricModelsValues[pc][i] * geometricModelsValues[pc][j];
                    }
                }
//                System.out.println("matrixCForFourPoints " + pc);
//                MatrixService.showTable2Dshort(matrixCForFourPoints[pc]);
            }
            double[][] dNdKsiTable = new double[length][4]; // table1 - strona 13 calkowanie macierzy H
            double[][] dNdEtaTable = new double[length][4];
            List<Function<Double, Double>> functions_dNdEta = GlobalData.getFunctions_dNdEta();
            List<Function<Double, Double>> functions_dNdKsi = GlobalData.getFunctions_dNdKsi();
            for (int pc = 0; pc < length; pc++) {
                for (int j = 0; j < 4; j++) {
                    dNdKsiTable[pc][j] = functions_dNdEta.get(j).apply(eta[pc]);
                    dNdEtaTable[pc][j] = functions_dNdKsi.get(j).apply(ksi[pc]);
                }
            }


            double[][][] jakobianMatrix = new double[length][2][2]; // length for every pc
            for (int pc = 0; pc < length; pc++) { // for every pc // for example 4 pc
                for (int j = 0; j < 4; j++) {
                    jakobianMatrix[pc][0][0] += dNdKsiTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][0][1] += dNdKsiTable[pc][j] * pcy[j];
                    jakobianMatrix[pc][1][0] += dNdEtaTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][1][1] += dNdEtaTable[pc][j] * pcy[j];
                }
            }
            //===================Calculation for determinant===========
            double[] determinants = new double[length]; // for every pc in element... so for 2 pc there will be 4, for 4 pc -> 9
            for (int pc = 0; pc < length; pc++) {
                determinants[pc] = jakobianMatrix[pc][0][0] * jakobianMatrix[pc][1][1] - jakobianMatrix[pc][0][1] * jakobianMatrix[pc][1][0]; // 0,00015625
            }

            double[] reverseDeterminants = new double[length];
            for (int i = 0; i < reverseDeterminants.length; i++) {
                reverseDeterminants[i] = 1.0 / determinants[i];
            }
            double[][][] jakobianMatrixMultipliedByReverseDetJ = new double[length][2][2];
            for (int pc = 0; pc < length; pc++) {
                jakobianMatrixMultipliedByReverseDetJ[pc][0][0] = reverseDeterminants[pc] * jakobianMatrix[pc][1][1];
                jakobianMatrixMultipliedByReverseDetJ[pc][0][1] = reverseDeterminants[pc] * (-jakobianMatrix[pc][0][1]); // have to be a minus
                jakobianMatrixMultipliedByReverseDetJ[pc][1][0] = reverseDeterminants[pc] * jakobianMatrix[pc][1][0];
                jakobianMatrixMultipliedByReverseDetJ[pc][1][1] = reverseDeterminants[pc] * (-jakobianMatrix[pc][0][0]);
            }

//            System.out.println("Macierze jakobiego dla ");
//            for (int pc = 0; pc < length; pc++) {
//                System.out.println("pc " + (pc + 1) + "element " + (e + 1));
//                MatrixService.showTable2Dshort(jakobianMatrixMultipliedByReverseDetJ[pc]);
//                System.out.println();
//            }
            //===================Two point integration==========

            double[][] dNiDividedByDx = new double[length][4]; //dNiDividedByDx
            double[][] dNiDividedByDy = new double[length][4]; //dNiDividedByDx
            for (int pc = 0; pc < length; pc++) {
                for (int j = 0; j < 4; j++) {
                    dNiDividedByDx[pc][j] = jakobianMatrixMultipliedByReverseDetJ[pc][0][0] * dNdKsiTable[pc][j]
                            + jakobianMatrixMultipliedByReverseDetJ[pc][0][1] * dNdEtaTable[pc][j];
                    dNiDividedByDy[pc][j] = jakobianMatrixMultipliedByReverseDetJ[pc][1][0] * dNdKsiTable[pc][j]
                            + jakobianMatrixMultipliedByReverseDetJ[pc][1][1] * dNdEtaTable[pc][j];
                }
            }

            double[][][] table1 = new double[length][4][4];
            double[][][] table2 = new double[length][4][4];
            for (int pc = 0; pc < length; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        table1[pc][i][j] = dNiDividedByDx[pc][i] * dNiDividedByDx[pc][j];
                        table2[pc][i][j] = dNiDividedByDy[pc][i] * dNiDividedByDy[pc][j];
                        Hpcs[pc][i][j] = table1[pc][i][j] + table2[pc][i][j];
                        Hpcs[pc][i][j] = Hpcs[pc][i][j] * GlobalData.conductivity * determinants[pc];
                    }
                }
            }

            double[][] localMatrixCForElement = new double[4][4];
            double[][] localHForElement = new double[4][4];
            for (int pc = 0; pc < length; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        localHForElement[i][j] += Hpcs[pc][i][j] * weightsOfPoints[pc / numberOfNodes] * weightsOfPoints[pc % numberOfNodes];
                        // bellow example for 3 numberOfNodes
//                            localHForElement[i][j] = Hpcs[0][i][j] * weightsOfPoints[0] * weightsOfPoints[0]
//                                    + Hpcs[1][i][j] * weightsOfPoints[1] * weightsOfPoints[0]
//                                    + Hpcs[2][i][j] * weightsOfPoints[2] * weightsOfPoints[0]
//                                    + Hpcs[3][i][j] * weightsOfPoints[0] * weightsOfPoints[1]
//                                    + Hpcs[4][i][j] * weightsOfPoints[1] * weightsOfPoints[1]
//                                    + Hpcs[5][i][j] * weightsOfPoints[2] * weightsOfPoints[1]
//                                    + Hpcs[6][i][j] * weightsOfPoints[0] * weightsOfPoints[2]
//                                    + Hpcs[7][i][j] * weightsOfPoints[1] * weightsOfPoints[2]
//                                    + Hpcs[8][i][j] * weightsOfPoints[2] * weightsOfPoints[2];
                        localMatrixCForElement[i][j] = specificHeat * density * determinants[pc] * matrixCForFourPoints[0][i][j] // sum for one element
                                + specificHeat * density * determinants[pc] * matrixCForFourPoints[1][i][j]
                                + specificHeat * density * determinants[pc] * matrixCForFourPoints[2][i][j]
                                + specificHeat * density * determinants[pc] * matrixCForFourPoints[3][i][j]; //FIXME for different pc!!!!!!
                    }
                }
            }
//            System.out.println("Local matrix H for element " + (e + 1));
//            MatrixService.showTable2Dshort(localHForElement);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationH[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += localHForElement[i][j];
                }
            }

//            System.out.println("localMatrixCForElement: ");
//            MatrixService.showTable2D(localMatrixCForElement); // for one element
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationMatrixC[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += localMatrixCForElement[i][j]; // without + !!! just = ???
                }
            }


        }
//        System.out.println("Global Aggregation matrix H");
//        MatrixService.showTable2Dshort(globalAggregationH);
//        System.out.println("Global Aggregation matrix C");
//        MatrixService.showTable2Dshort(globalAggregationMatrixC);
    }

    public static double[][] getTableOfEta2IntegralMultipliedByMatrix(double[][] mainMatrix, double[][] tableOfKsiIntegral, double[][] tableOfEtaIntegral) {
        double[][] table2DlaPc1 = new double[tableOfKsiIntegral.length][4];
        for (int i = 0; i < tableOfKsiIntegral.length; i++) {
            for (int j = 0; j < 4; j++) {
                table2DlaPc1[i][j] = mainMatrix[1][0] * tableOfKsiIntegral[i][j] + mainMatrix[1][1] * tableOfEtaIntegral[i][j];
            }
        }
        return table2DlaPc1;
    }

    public static double[][] getTableOfKsi1IntegralMultipliedByMatrix(double[][] mainMatrix, double[][] tableOfKsiIntegral, double[][] tableOfEtaIntegral) {
        double[][] table1DlaPc1 = new double[tableOfKsiIntegral.length][4];
        for (int i = 0; i < tableOfKsiIntegral.length; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaPc1[i][j] = mainMatrix[0][0] * tableOfKsiIntegral[i][j] + mainMatrix[0][1] * tableOfEtaIntegral[i][j];
            }
        }
        return table1DlaPc1;
    }
}
