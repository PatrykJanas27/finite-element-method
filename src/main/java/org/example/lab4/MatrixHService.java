package org.example.lab4;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class MatrixHService {
    public static double[][] globalAggregationH = new double[16][16];

    public static double[][] getMatrixHWithGlobalData(Grid grid, int numberOfNodes) {
//        double[][] globalAggregationH = new double[grid.getNodesNumber()][grid.getNodesNumber()];
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
            // Here for matrix H
            double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
            double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
//==============================
//            double[][] dNdKsiTable = new double[4][4]; // table1 - strona 13 calkowanie macierzy H
//            double[][] dNdEtaTable = new double[4][4];
//            List<Function<Double, Double>> functions_dNdKsi = GlobalData.getFunctions_dNdKsi();
//            List<Function<Double, Double>> functions_dNdEta = GlobalData.getFunctions_dNdEta();
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    dNdKsiTable[i][j] = functions_dNdKsi.get(j).apply(ksi[i]);
//                    dNdEtaTable[i][j] = functions_dNdEta.get(j).apply(eta[i]);
//                }
//            }
            //==============================
            double[][] dNdKsiTable = new double[][]{
                    {-0.39434, 0.39434, 0.105662, -0.105662},
                    {-0.39434, 0.39434, 0.105662, -0.105662},
                    {-0.105662, 0.105662, 0.39434, -0.39434},
                    {-0.105662, 0.105662, 0.39434, -0.39434}
            };
            double[][] dNdEtaTable = new double[][]{
                    {-0.39434, -0.105662, 0.105662, 0.39434},
                    {-0.105662, -0.39434, 0.39434, 0.105662},
                    {-0.39434, -0.105662, 0.105662, 0.39434},
                    {-0.105662, -0.39434, 0.39434, 0.105662}
            };

            double[][][] jakobianMatrix = new double[length][2][2]; // length for every pc
            double m00 = 0;
//            for (int i = 0; i < 4; i++) {
//                m00 += dNdKsiTable[0][i] * pcx[i];
//            }
            for (int pc = 0; pc < length; pc++) { // for every pc // for example 4 pc
                for (int j = 0; j < 4; j++) {
                    jakobianMatrix[pc][0][0] += dNdKsiTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][0][1] += dNdKsiTable[pc][j] * pcy[j];
                    jakobianMatrix[pc][1][0] += dNdEtaTable[pc][j] * pcx[j];
                    jakobianMatrix[pc][1][1] += dNdEtaTable[pc][j] * pcy[j];
                }
            }
//            System.out.println("main Matrix for element " + (e+1));
//            MatrixService.showTable2Dshort(jakobianMatrix);

            //-0.01666666545    0.0
            // 0.0              -0.016666667094999997
            //OR
            //0,0125            0.0
            //0.0               0.0125
            //===================Calculation for determinant===========
            double[] determinants = new double[length]; // for every pc in element... so for 2 pc there will be 4, for 4 pc -> 9
            for (int pc = 0; pc < length; pc++) {
                determinants[pc] = jakobianMatrix[pc][0][0] * jakobianMatrix[pc][1][1] - jakobianMatrix[pc][0][1] * jakobianMatrix[pc][1][0]; // 0,00015625
            }

            // jakobianMatrix for pc1
            // now will be // data from pdf
            // 80 0
            // 0 80
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

            System.out.println("Macierze jakobiego dla ");
            for (int pc = 0; pc < length; pc++) {
                System.out.println("pc " + (pc + 1) + "element " + (e + 1));
                MatrixService.showTable2Dshort(jakobianMatrixMultipliedByReverseDetJ[pc]);
                System.out.println();
            }

            // TODO to jest to samo co wyzej dNdKsiTable[i][j] = functions_dNdKsi.get(j).apply(eta[i]);
            //                    dNdEtaTable[i][j] = functions_dNdEta.get(j).apply(ksi[i]);
            double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi(numberOfNodes);
            double[][] tableOfEtaIntegral = IntegralFunctions.getTableForDnDividedByDEta(numberOfNodes);//FIXME here is a bug
            //===================Two point integration==========

            double[][] dNiDividedByDx = new double[4][4]; //dNiDividedByDx
            double[][] dNiDividedByDy = new double[4][4]; //dNiDividedByDx
            for (int pc = 0; pc < length; pc++) {
                for (int i = 0; i < length; i++) {
                    for (int j = 0; j < 4; j++) {
                        //strona 13/19 calkowanie macierzy H
//                        //                                    //jakobiany te same      //tu sie przesuwa w prawo                //tu sie przesuwa w prawo
//                        dNiDividedByDx[pc][0][0] = jakobianMatrix[pc][0][0]*tableOfKsiIntegral[0][0]+jakobianMatrix[pc][0][1]*tableOfEtaIntegral[0][0];
//                        dNiDividedByDx[pc][0][1] = jakobianMatrix[pc][0][0]*tableOfKsiIntegral[0][1]+jakobianMatrix[pc][0][1]*tableOfEtaIntegral[0][1]; //jakobiany te same
//                        dNiDividedByDx[pc][0][2] = jakobianMatrix[pc][0][0]*tableOfKsiIntegral[0][2]+jakobianMatrix[pc][0][1]*tableOfEtaIntegral[0][2];
//                        dNiDividedByDx[pc][0][3] = jakobianMatrix[pc][0][0]*tableOfKsiIntegral[0][3]+jakobianMatrix[pc][0][1]*tableOfEtaIntegral[0][3];
//                        dNiDividedByDx[i][j] = jakobianMatrix[pc][0][0] * dNdKsiTable[][j] + jakobianMatrix[pc][0][1] * dNdEtaTable[i][j];
//                        dNiDividedByDy[i][j] = jakobianMatrix[pc][0][0] * dNdKsiTable[][j] + jakobianMatrix[pc][0][1] * dNdEtaTable[i][j];


                        dNiDividedByDx[pc][j] = jakobianMatrixMultipliedByReverseDetJ[pc][0][0] * dNdKsiTable[pc][j] + jakobianMatrixMultipliedByReverseDetJ[pc][0][1] * dNdEtaTable[pc][j];
                        dNiDividedByDy[pc][j] = jakobianMatrixMultipliedByReverseDetJ[pc][1][0] * dNdKsiTable[pc][j] + jakobianMatrixMultipliedByReverseDetJ[pc][1][1] * dNdEtaTable[pc][j];

//                        //dla pc1 Dx
//                        dNiDividedByDx[0][0] = jakobianMatrix[0][0][0] * dNdKsiTable[0][0] + jakobianMatrix[0][0][1] * dNdEtaTable[0][0];
//                        dNiDividedByDx[0][1] = jakobianMatrix[0][0][0] * dNdKsiTable[0][1] + jakobianMatrix[0][0][1] * dNdEtaTable[0][1];
//                        dNiDividedByDx[0][2] = jakobianMatrix[0][0][0] * dNdKsiTable[0][2] + jakobianMatrix[0][0][1] * dNdEtaTable[0][2];
//                        dNiDividedByDx[0][3] = jakobianMatrix[0][0][0] * dNdKsiTable[0][3] + jakobianMatrix[0][0][1] * dNdEtaTable[0][3];
//                        //dla pc1 Dy
//                        dNiDividedByDy[0][0] = jakobianMatrix[0][1][0] * dNdKsiTable[0][0] + jakobianMatrix[0][1][1] * dNdEtaTable[0][0];
//                        dNiDividedByDy[0][1] = jakobianMatrix[0][1][0] * dNdKsiTable[0][1] + jakobianMatrix[0][1][1] * dNdEtaTable[0][1];
//                        dNiDividedByDy[0][2] = jakobianMatrix[0][1][0] * dNdKsiTable[0][2] + jakobianMatrix[0][1][1] * dNdEtaTable[0][2];
//                        dNiDividedByDy[0][3] = jakobianMatrix[0][1][0] * dNdKsiTable[0][3] + jakobianMatrix[0][1][1] * dNdEtaTable[0][3];
//                        //dla pc2 Dx
//                        dNiDividedByDx[1][0] = jakobianMatrix[1][0][0] * dNdKsiTable[0][0] + jakobianMatrix[1][0][1] * dNdEtaTable[0][0];
                    }
                }
            }

            //===================WeightsOfPoints=======

            //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
//        double[][][] table2DlaH = new double[4][4];
//        double[][] Hpc = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table1DlaH[i][j] = table1DlaPc1[whichPc][i] * table1DlaPc1[whichPc][j];//zmiana dla Hpc2
//                table2DlaH[i][j] = table2DlaPc1[whichPc][i] * table2DlaPc1[whichPc][j];
//                Hpc[i][j] = GlobalData.conductivity * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
//            }
//        }
//            double[][][] table1DlaH = new double[length][4][4];
//            double[][][] table2DlaH = new double[length][4][4];
//            for (int pc = 0; pc < length; pc++) { // for every pc
//                for (int i = 0; i < length; i++) {
//                    for (int j = 0; j < 4; j++) {
////                Hpcs[i] = calculateHpcWithGlobalData(dNiDividedByDx, dNiDividedByDy, determinants[i], i);
////                        Hpcs[pc][i][j] += GlobalData.conductivity * (dNiDividedByDx[pc][i][j] * dNiDividedByDx[pc][i][j] + dNiDividedByDy[pc][i][j] * dNiDividedByDy[pc][i][j]) * determinants[pc];
//                    table1DlaH[pc][i][j] = dNiDividedByDx[pc][i][]
//                    }
//                }
//            }
//            for (int pc = 0; pc < length; pc++) {
//                Hpcs[pc] = calculateHpcWithGlobalData(dNiDividedByDx[pc], dNiDividedByDy[pc], determinants[pc], pc);
//            }
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
//            table1[0][0][0] = dNiDividedByDx[0][0][0] * dNiDividedByDx[0][0][0];
//            table1[0][0][1] = dNiDividedByDx[0][0][0] * dNiDividedByDx[0][0][1];
//            table1[0][0][2] = dNiDividedByDx[0][0][0] * dNiDividedByDx[0][0][2];
//            table1[0][0][3] = dNiDividedByDx[0][0][0] * dNiDividedByDx[0][0][3];
//            table1[0][1][0] = dNiDividedByDx[0][0][1] * dNiDividedByDx[0][0][0];
//            table1[0][1][1] = dNiDividedByDx[0][0][1] * dNiDividedByDx[0][0][1];
//            table1[0][1][2] = dNiDividedByDx[0][0][1] * dNiDividedByDx[0][0][2];
//            table1[0][1][3] = dNiDividedByDx[0][0][1] * dNiDividedByDx[0][0][3];


            //=====showing local matrix H ====
            double[][] localHForElement = new double[4][4];
            for (int pc = 0; pc < length; pc++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
//                        localHForElement[i][j] += Hpcs[pc][i][j];
                        localHForElement[i][j] = Hpcs[0][i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                                + Hpcs[1][i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                                + Hpcs[2][i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                                + Hpcs[3][i][j] * weightsOfPoints[0] * weightsOfPoints[1];
                    }
                }
            }
            System.out.println("Hpc1 dla elementu - " + (e + 1));
            MatrixService.showTable2D(Hpcs[0]);
            System.out.println("Hpc2 dla elementu - " + (e + 1));
            MatrixService.showTable2D(Hpcs[1]);
            System.out.println("Hpc3 dla elementu - " + (e + 1));
            MatrixService.showTable2D(Hpcs[2]);
            System.out.println("Hpc4 dla elementu - " + (e + 1));
            MatrixService.showTable2D(Hpcs[3]);
            System.out.println("H dla elementu - " + (e + 1));
            MatrixService.showTable2Dshort(localHForElement);
            System.out.println("local determinants: ");
            MatrixService.showTable1D(determinants);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationH[element.getIDs().get(i) - 1][element.getIDs().get(j) - 1] += localHForElement[i][j];
                }
//                System.out.println();
            }


//            MatrixService.showTable1D(determinants);

        }
        MatrixService.showTable2Dshort(globalAggregationH);
        //   ===================tables' summing to H=======

        double[][] mainH = new double[4][4];
//        for (int k = 0; k < length; k++) {
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                    mainH[i][j] += Hpcs[k][i][j] * weightsOfPoints[k / numberOfNodes] * weightsOfPoints[k % numberOfNodes];
//                }
//
//            }
//        }
        return globalAggregationH;
    }

//    public static double[][] getMatrixHWithGlobalData(Grid grid, int numberOfNodes) {
//        List<Element> elements = grid.getElements();
//        List<Node> nodes = grid.getNodes();
//        int length = numberOfNodes * numberOfNodes;
//        int elementsNumber = grid.getElementsNumber();
//        double[][][] Hpcs = new double[length][4][4];
//
//        // --> main loop for elements
//        for (int e = 0; e < grid.getElements().size(); e++) {
//            Element element = elements.get(e);
//            double[] pcx = new double[4];
//            double[] pcy = new double[4];
//            for (int i = 0; i < nodes.size(); i++) { // < 4
//                for (int j = 0; j < 4; j++) { // 4 wspolrzedne x i y
//                    pcx[j] = nodes.get(element.getIDs().get(j) - 1).getX();
//                    pcy[j] = nodes.get(element.getIDs().get(j) - 1).getY();
//                }
//            }
//            // Here for matrix H
//            double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
//            double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
//            double[][] dNdKsiTable = new double[4][4]; // table1 - strona 13 calkowanie macierzy H
//            double[][] dNdEtaTable = new double[4][4];
//            List<Function<Double, Double>> functions1 = GlobalData.getFunctionsForKsi();
//            List<Function<Double, Double>> functions2 = GlobalData.getFunctionsForEta();
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    dNdKsiTable[i][j] = functions1.get(j).apply(eta[i]);
//                    dNdEtaTable[i][j] = functions2.get(j).apply(ksi[i]);
//                }
//            }
//
//            double[][] mainMatrix = new double[2][2];
//            for (int j = 0; j < 4; j++) {
//                mainMatrix[0][0] += dNdKsiTable[0][j] * pcx[j];
//                mainMatrix[0][1] += dNdKsiTable[0][j] * pcy[j];
//                mainMatrix[1][0] += dNdEtaTable[0][j] * pcx[j];
//                mainMatrix[1][1] += dNdEtaTable[0][j] * pcy[j];
//            }
//            System.out.println("main Matrix for element " + (e+1));
//            MatrixService.showTable2Dshort(mainMatrix);
//            //-0.01666666545    0.0
//            // 0.0              -0.016666667094999997
//            //===================Calculation for determinant===========
//            double[] determinants = new double[length]; // for every pc in element... so for 2 pc there will be 4, for 4 pc -> 9
//            double determinant =mainMatrix[0][0] * mainMatrix[1][1] - mainMatrix[0][1] * mainMatrix[1][0]; // 0,00015625
//            // mainMatrix for pc1
//            // 80 0
//            // 0 80
//            for (int i = 0; i < 2; i++) {
//                for (int j = 0; j < 2; j++) {
//                    mainMatrix[i][j] = (1.0 / determinant) * mainMatrix[i][j];
//                }
//            }
//
//            double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi(numberOfNodes);
//            double[][] tableOfEtaIntegral = IntegralFunctions.getTableForDnDividedByDEta(numberOfNodes);
//            //===================Two point integration==========
//            double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//            double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//            //===================WeightsOfPoints=======
//
//            //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
//            for (int i = 0; i < length; i++) {
//                Hpcs[i] = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, i);
//            }
//
//            //=====showing local matrix H ====
//            double[][] localHForElement= new double[4][4];
//            for (int i = 0; i < length; i++) {
//                for (int j = 0; j < 4; j++) {
//                    for (int k = 0; k < 4; k++) {
//                        localHForElement[j][k]+=Hpcs[i][j][k];
//                    }
//                }
//            }
//            System.out.println("H dla elementu - " + (e+1));
//            MatrixService.showTable2Dshort(localHForElement);
//            System.out.println("local determinant: " + determinant);
//        }

//    public static double[][] getMatrixHForTwoPointIntegrationWithGlobalData(double[][] mainMatrix) {
//        //===================Calculation for determinant===========
//        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
//        // mainMatrix
//        // 80 0
//        // 0 80
//        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);
//
//        double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi(2);
//        double[][] tableOfEtaIntegral = IntegralFunctions.getTableForDnDividedByDEta(2);
//        //===================Two point integration==========
//        double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//        double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
////        System.out.println("tableOfKsiIntegralByM 1: ");
////        MatrixService.showTable2D(tableOfKsiIntegralByM);
////        System.out.println("tableOfEtaIntegralByM 2:");
////        MatrixService.showTable2D(tableOfEtaIntegralByM);
//        //===================WeightsOfPoints=======
//        double[] weightsOfPoints = GlobalData.getWeightsArray(2); //{1.0, 1.0}
//        //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
//        double[][] Hpc1 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 0);
//        double[][] Hpc2 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 1);
//        double[][] Hpc3 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 2);
//        double[][] Hpc4 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 3);
//        System.out.println("Matrix H1");
//        MatrixService.showTable2Dshort(Hpc1);
//        System.out.println("Matrix H2");
//        MatrixService.showTable2Dshort(Hpc2);
//        System.out.println("Matrix H3");
//        MatrixService.showTable2Dshort(Hpc3);
//        System.out.println("Matrix H4");
//        MatrixService.showTable2Dshort(Hpc4);
//        //===================tables' summing to H=======
//        double[][] mainH = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                mainH[i][j] = Hpc1[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
//                        + Hpc2[i][j] * weightsOfPoints[0] * weightsOfPoints[1]
//                        + Hpc3[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
//                        + Hpc4[i][j] * weightsOfPoints[0] * weightsOfPoints[1];
//            }
//        }
//        return mainH;
//    }


//    private static double[][] calculateHpcWithGlobalData(double[][] dNiDividedByDx, double[][] dNiDividedByDy, double detJ, int whichPc) {
//        double[][] table1DlaH = new double[4][4];
//        double[][] table2DlaH = new double[4][4];
//        double[][] Hpc = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table1DlaH[i][j] = dNiDividedByDx[whichPc][i] * dNiDividedByDx[whichPc][j];//zmiana dla Hpc2
//                table2DlaH[i][j] = dNiDividedByDy[whichPc][i] * dNiDividedByDy[whichPc][j];
//                Hpc[i][j] = GlobalData.conductivity * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
//            }
//        }
//        return Hpc;
//    }


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
