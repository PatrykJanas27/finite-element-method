package org.example.lab4;

import org.example.lab1.GlobalData;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;


public class Main {
    public static void main(String[] args) {
        double[][] matrixHForTwoPointIntegration = getMatrixH(2);
        System.out.println("MatrixHForTwoPointIntegration: ");
        MatrixService.showTable2D(matrixHForTwoPointIntegration);

        double[][] matrixHForThreePointIntegration = getMatrixH(3);
        System.out.println("MatrixHForThreePointIntegration: ");
        MatrixService.showTable2D(matrixHForThreePointIntegration);

        double[][] matrixHForFourPointIntegration = getMatrixH(4);
        System.out.println("MatrixHForFourPointIntegration: ");
        MatrixService.showTable2D(matrixHForFourPointIntegration);

    }

    private static double[][] getMatrixH(int numberOfNodes) {
        double[][] mainMatrix = getMainMatrix();
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        // mainMatrix
        // 80 0
        // 0 80
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);

        double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi(numberOfNodes);
        double[][] tableOfEtaIntegral = IntegralFunctions.getTableForDnDividedByDEta(numberOfNodes);

        double[][] tableOfKsiIntegralByM = MatrixHService.getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = MatrixHService.getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
        int length = numberOfNodes * numberOfNodes;
        double[][][] Hpcs = new double[length][4][4];
        for (int i = 0; i < length; i++) {
            Hpcs[i] = calculateHpc(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, i);
        }
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GlobalData.getWeightsArray(numberOfNodes); //{5.0 / 9.0, 8.0 / 9, 5.0 / 9}
        //
        double[][] mainH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < length; k++) {
                    mainH[i][j] += Hpcs[k][i][j] * weightsOfPoints[k / numberOfNodes] * weightsOfPoints[k % numberOfNodes];
                }
            }
        }
        return mainH;
    }

    private static double[][] calculateHpc(double[][] table1DlaPc1, double[][] table2DlaPc1, double detJ, int whichPc) {
        double[][] table1DlaH = new double[4][4];
        double[][] table2DlaH = new double[4][4];
        double[][] Hpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaH[i][j] = table1DlaPc1[whichPc][i] * table1DlaPc1[whichPc][j];
                table2DlaH[i][j] = table2DlaPc1[whichPc][i] * table2DlaPc1[whichPc][j];
                Hpc[i][j] = 30 * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
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
