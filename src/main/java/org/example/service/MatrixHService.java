package org.example.service;

import org.example.model.Element;
import org.example.model.GlobalData;
import org.example.model.Grid;
import org.example.model.Node;

import java.util.List;
import java.util.function.Function;

import static org.example.model.GlobalData.geometricModelsN;

public class MatrixHService {
    public static double[][] globalAggregationH;
    public static double[][] globalAggregationMatrixC;

    public static void calculate_MatrixH_and_MatrixC(Grid grid, int numberOfNodes) {
        if (numberOfNodes < 2 || numberOfNodes > 4) {
            throw new IllegalArgumentException("" +
                    "There is no method to calculate matrixH and matrixC with number for nodes = " + numberOfNodes);
        }
        int density = GlobalData.density;
        int specificHeat = GlobalData.specificHeat;
        globalAggregationH = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        globalAggregationMatrixC = new double[grid.getNodesNumber()][grid.getNodesNumber()];

        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();
        int length = numberOfNodes * numberOfNodes; //for numberOfNodes=4 length is 16
        double[][][] Hpcs = new double[length][4][4];
        double[] weightsOfPoints = GlobalData.getWeightsArray(numberOfNodes);

        // **************** main loop for elements ******************
        for (int e = 0; e < grid.getElements().size(); e++) {
            Element element = elements.get(e);
            double[] pcx = new double[4];
            double[] pcy = new double[4];
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    pcx[j] = nodes.get(element.getIDs().get(j) - 1).getX();
                    pcy[j] = nodes.get(element.getIDs().get(j) - 1).getY();
                }
            }
            double[] ksi = GlobalData.getKsiCoordinates(numberOfNodes);
            double[] eta = GlobalData.getEtaCoordinates(numberOfNodes);
            // Geometrics models for matrix C **************** (ksi, eta) -> 0.25 * (1 - ksi) * (1 - eta))
            // Values for geometric models         //{N}*{N}^T
            double[][] geometricModelsValues = new double[length][4];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < 4; j++) {
                    geometricModelsValues[i][j] = geometricModelsN(j, ksi[i], eta[i]);
                }
            }
            double[][][] matrixCForFourPoints = new double[length][4][4]; // there will be 9 matrix C for every point if numberOfNodes = 3
            for (int pc = 0; pc < length; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        matrixCForFourPoints[pc][i][j] =
                                geometricModelsValues[pc][i] * geometricModelsValues[pc][j];
                    }
                }
            }
            double[][] dNdKsiTable = new double[length][4];
            double[][] dNdEtaTable = new double[length][4];
            List<Function<Double, Double>> functions_dNdEta = GlobalData.getFunctions_dNdEta();
            List<Function<Double, Double>> functions_dNdKsi = GlobalData.getFunctions_dNdKsi();
            for (int pc = 0; pc < length; pc++) {
                for (int j = 0; j < 4; j++) {
                    dNdKsiTable[pc][j] = functions_dNdEta.get(j).apply(eta[pc]);
                    dNdEtaTable[pc][j] = functions_dNdKsi.get(j).apply(ksi[pc]);
                }
            }

            double[][][] jakobianMatrix = new double[length][2][2];
            for (int pc = 0; pc < length; pc++) {
                for (int j = 0; j < 4; j++) {
                    jakobianMatrix[pc][0][0] += dNdKsiTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][0][1] += dNdKsiTable[pc][j] * pcy[j];
                    jakobianMatrix[pc][1][0] += dNdEtaTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][1][1] += dNdEtaTable[pc][j] * pcy[j];
                }
            }
            //===================Calculation for determinants===========
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
                jakobianMatrixMultipliedByReverseDetJ[pc][1][0] = reverseDeterminants[pc] * (-jakobianMatrix[pc][1][0]);
                jakobianMatrixMultipliedByReverseDetJ[pc][1][1] = reverseDeterminants[pc] * jakobianMatrix[pc][0][0];
            }

            double[][] dNiDividedByDx = new double[length][4];
            double[][] dNiDividedByDy = new double[length][4];
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
            for (int pc = 0; pc < length; pc++) { // for calculating matrix H
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
                        if (numberOfNodes == 2) {
                            localMatrixCForElement[i][j] = specificHeat * density * determinants[0] * matrixCForFourPoints[0][i][j] // sum for one element
                                    + specificHeat * density * determinants[1] * matrixCForFourPoints[1][i][j]
                                    + specificHeat * density * determinants[2] * matrixCForFourPoints[2][i][j]
                                    + specificHeat * density * determinants[3] * matrixCForFourPoints[3][i][j];

                        }
                        if (numberOfNodes == 3) {
                            localMatrixCForElement[i][j] =
                                    specificHeat * density * determinants[0] * matrixCForFourPoints[0][i][j] * weightsOfPoints[0] * weightsOfPoints[0]  // sum for one element
                                            + specificHeat * density * determinants[1] * matrixCForFourPoints[1][i][j] * weightsOfPoints[1] * weightsOfPoints[0]
                                            + specificHeat * density * determinants[2] * matrixCForFourPoints[2][i][j] * weightsOfPoints[2] * weightsOfPoints[0]
                                            + specificHeat * density * determinants[3] * matrixCForFourPoints[3][i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[4] * matrixCForFourPoints[4][i][j] * weightsOfPoints[1] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[5] * matrixCForFourPoints[5][i][j] * weightsOfPoints[2] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[6] * matrixCForFourPoints[6][i][j] * weightsOfPoints[0] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[7] * matrixCForFourPoints[7][i][j] * weightsOfPoints[1] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[8] * matrixCForFourPoints[8][i][j] * weightsOfPoints[2] * weightsOfPoints[2];
                        }
                        if (numberOfNodes == 4) {
                            localMatrixCForElement[i][j] =
                                    specificHeat * density * determinants[0] * matrixCForFourPoints[0][i][j] * weightsOfPoints[0] * weightsOfPoints[0]  // sum for one element
                                            + specificHeat * density * determinants[1] * matrixCForFourPoints[1][i][j] * weightsOfPoints[1] * weightsOfPoints[0]
                                            + specificHeat * density * determinants[2] * matrixCForFourPoints[2][i][j] * weightsOfPoints[2] * weightsOfPoints[0]
                                            + specificHeat * density * determinants[3] * matrixCForFourPoints[3][i][j] * weightsOfPoints[3] * weightsOfPoints[0]
                                            + specificHeat * density * determinants[4] * matrixCForFourPoints[4][i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[5] * matrixCForFourPoints[5][i][j] * weightsOfPoints[1] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[6] * matrixCForFourPoints[6][i][j] * weightsOfPoints[2] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[7] * matrixCForFourPoints[7][i][j] * weightsOfPoints[3] * weightsOfPoints[1]
                                            + specificHeat * density * determinants[8] * matrixCForFourPoints[8][i][j] * weightsOfPoints[0] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[9] * matrixCForFourPoints[9][i][j] * weightsOfPoints[1] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[10] * matrixCForFourPoints[10][i][j] * weightsOfPoints[2] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[11] * matrixCForFourPoints[11][i][j] * weightsOfPoints[3] * weightsOfPoints[2]
                                            + specificHeat * density * determinants[12] * matrixCForFourPoints[12][i][j] * weightsOfPoints[0] * weightsOfPoints[3]
                                            + specificHeat * density * determinants[13] * matrixCForFourPoints[13][i][j] * weightsOfPoints[1] * weightsOfPoints[3]
                                            + specificHeat * density * determinants[14] * matrixCForFourPoints[14][i][j] * weightsOfPoints[2] * weightsOfPoints[3]
                                            + specificHeat * density * determinants[15] * matrixCForFourPoints[15][i][j] * weightsOfPoints[3] * weightsOfPoints[3];
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationH[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += localHForElement[i][j];
                    globalAggregationMatrixC[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += localMatrixCForElement[i][j];
                }
            }
        }
    }

}
