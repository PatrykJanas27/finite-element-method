package org.example.lab4;

import org.example.lab1.GlobalData;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;


public class Main {
    public static void main(String[] args) {
        double[][] matrixHForTwoPointIntegration = getMatrixHForTwoPointIntegration();
        System.out.println("MatrixHForTwoPointIntegration: ");
        MatrixService.showTable2D(matrixHForTwoPointIntegration);

        double[][] matrixHForThreePointIntegration = getMatrixHForThreePointIntegration();
        System.out.println("MatrixHForThreePointIntegration: ");
        MatrixService.showTable2D(matrixHForThreePointIntegration);

        double[][] matrixHForFourPointIntegration = getMatrixHForFourPointIntegration();
        System.out.println("MatrixHForFourPointIntegration: ");
        MatrixService.showTable2D(matrixHForFourPointIntegration);
    }
    private static double[][] getMatrixHForTwoPointIntegration() {
        double[][] mainMatrix = getMainMatrix();
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);

        double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowSecondArray();
        //===================Two point integration==========
        double[][] tableOfKsiIntegralByM = MatrixHService.getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = MatrixHService.getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        System.out.println("tableOfKsiIntegralByM 1: ");
        MatrixService.showTable2D(tableOfKsiIntegralByM);
        System.out.println("tableOfEtaIntegralByM 2:");
        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GlobalData.getWeightsArray(2); //{1.0, 1.0}
        //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
        double[][] Hpc1 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 0);
        double[][] Hpc2 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 1);
        double[][] Hpc3 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 2);
        double[][] Hpc4 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 3);

        //===================tables' summing to H=======
        double[][] mainH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mainH[i][j] = Hpc1[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                        + Hpc2[i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                        + Hpc3[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                        + Hpc4[i][j] * weightsOfPoints[0] * weightsOfPoints[1];
            }
        }
        return mainH;
    }

    private static double[][] getMatrixHForThreePointIntegration() {
        double[][] mainMatrix = getMainMatrix();
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);

        double[][] tableOfKsiIntegral = IntegralFunctions.calculateAndShowThirdArray();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowFourthArray();
        //===================Three point integration==========
        double[][] tableOfKsiIntegralByM = MatrixHService.getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = MatrixHService.getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GlobalData.getWeightsArray(3); //{5.0 / 9.0, 8.0 / 9, 5.0 / 9}
        //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
        double[][] Hpc1 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 0);
        double[][] Hpc2 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 1);
        double[][] Hpc3 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 2);
        double[][] Hpc4 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 3);
        double[][] Hpc5 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 4);
        double[][] Hpc6 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 5);
        double[][] Hpc7 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 6);
        double[][] Hpc8 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 7);
        double[][] Hpc9 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 8);

        //===================tables' summing to H=======
        double[][] mainH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mainH[i][j] = Hpc1[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                        + Hpc2[i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                        + Hpc3[i][j] * weightsOfPoints[0] * weightsOfPoints[2]
                        + Hpc4[i][j] * weightsOfPoints[1] * weightsOfPoints[0]
                        + Hpc5[i][j] * weightsOfPoints[1] * weightsOfPoints[1]
                        + Hpc6[i][j] * weightsOfPoints[1] * weightsOfPoints[2]
                        + Hpc7[i][j] * weightsOfPoints[2] * weightsOfPoints[0]
                        + Hpc8[i][j] * weightsOfPoints[2] * weightsOfPoints[1]
                        + Hpc9[i][j] * weightsOfPoints[2] * weightsOfPoints[2];
            }
        }
        return mainH;
    }

    private static double[][] getMatrixHForFourPointIntegration() {
        double[][] mainMatrix = getMainMatrix();
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);

        double[][] tableOfKsiIntegral = IntegralFunctions.calculateAndShowFifthArray();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowSixthArray();
        //===================Four point integration==========
        double[][] tableOfKsiIntegralByM = MatrixHService.getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = MatrixHService.getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GlobalData.getWeightsArray(4); //{5.0 / 9.0, 8.0 / 9, 5.0 / 9}
        //===================Calculating tables of H for tree point integration (there will be 16 tables)==========
        double[][] Hpc1 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 0);
        double[][] Hpc2 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 1);
        double[][] Hpc3 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 2);
        double[][] Hpc4 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 3);
        double[][] Hpc5 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 4);
        double[][] Hpc6 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 5);
        double[][] Hpc7 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 6);
        double[][] Hpc8 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 7);
        double[][] Hpc9 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 8);
        double[][] Hpc10 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 9);
        double[][] Hpc11 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 10);
        double[][] Hpc12 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 11);
        double[][] Hpc13 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 12);
        double[][] Hpc14 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 13);
        double[][] Hpc15 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 14);
        double[][] Hpc16 = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 15);

        //===================tables' summing to H=======
        double[][] mainH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mainH[i][j] = Hpc1[i][j] * weightsOfPoints[0] * weightsOfPoints[0]
                        + Hpc2[i][j] * weightsOfPoints[0] * weightsOfPoints[1]
                        + Hpc3[i][j] * weightsOfPoints[0] * weightsOfPoints[2]
                        + Hpc4[i][j] * weightsOfPoints[0] * weightsOfPoints[3]
                        + Hpc5[i][j] * weightsOfPoints[1] * weightsOfPoints[0]
                        + Hpc6[i][j] * weightsOfPoints[1] * weightsOfPoints[1]
                        + Hpc7[i][j] * weightsOfPoints[1] * weightsOfPoints[2]
                        + Hpc8[i][j] * weightsOfPoints[1] * weightsOfPoints[3]
                        + Hpc9[i][j] * weightsOfPoints[2] * weightsOfPoints[0]
                        + Hpc10[i][j] * weightsOfPoints[2] * weightsOfPoints[1]
                        + Hpc11[i][j] * weightsOfPoints[2] * weightsOfPoints[2]
                        + Hpc12[i][j] * weightsOfPoints[2] * weightsOfPoints[3]
                        + Hpc13[i][j] * weightsOfPoints[3] * weightsOfPoints[0]
                        + Hpc14[i][j] * weightsOfPoints[3] * weightsOfPoints[1]
                        + Hpc15[i][j] * weightsOfPoints[3] * weightsOfPoints[2]
                        + Hpc16[i][j] * weightsOfPoints[3] * weightsOfPoints[3];
            }
        }
        return mainH;
    }

    private static double[][] calculateHpc(double[][] table1DlaPc1, double[][] table2DlaPc1, double detJ, int whichPc) {
        double[][] table1DlaH = new double[4][4];
        double[][] table2DlaH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaH[i][j] = table1DlaPc1[whichPc][i] * table1DlaPc1[whichPc][j];//zmiana dla Hpc2
                table2DlaH[i][j] = table2DlaPc1[whichPc][i] * table2DlaPc1[whichPc][j];
            }
        }
        double[][] Hpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Hpc[i][j] = 30 * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ; // TODO read from file conductivity
            }
        }
        return Hpc;
    }

    private static double[][] getMainMatrix() {
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))}; // wspolrzedne punktow calkowania
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table1 = new double[4][4];
        double[][] table2 = new double[4][4];
        List<Function<Double, Double>> functions1 = GlobalData.getFunctionsForKsi();
        List<Function<Double, Double>> functions2 = GlobalData.getFunctionsForEta();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1[i][j] = functions1.get(j).apply(eta[i]);
                table2[i][j] = functions2.get(j).apply(ksi[i]);
            }
        }
        double[] pcx = getPcx();
        double[] pcy = getPcy();
        double[][] mainMatrix = new double[2][2];
        for (int j = 0; j < 4; j++) {
            mainMatrix[0][0] += table1[0][j] * pcx[j];
            mainMatrix[0][1] += table1[0][j] * pcy[j];
            mainMatrix[1][0] += table2[0][j] * pcx[j];
            mainMatrix[1][1] += table2[0][j] * pcy[j];
        }
        return mainMatrix;
    }

    private static double[] getPcx() {
        return new double[]{0, 0.025, 0.025, 0};
    }

    private static double[] getPcy() {
        return new double[]{0, 0, 0.025, 0.025};
    }



}
