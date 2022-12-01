package org.example.lab4;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class AppDla3pkt {
    public static double[][] mainMatrix = new double[2][2];
    public static void main(String[] args) throws FileNotFoundException {
        calculateAndShowFirstArray();
        calculateAndShowSecondArray();
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);


        double[][] matrixHForTwoPointIntegration = getMatrixHForTwoPointIntegration(determinant);
        System.out.println("MatrixHForTwoPointIntegration: ");
        MatrixService.showTable2D(matrixHForTwoPointIntegration);
        double[][] matrixHForThreePointIntegration = getMatrixHForThreePointIntegration(determinant);
        System.out.println("MatrixHForThreePointIntegration: ");
        MatrixService.showTable2D(matrixHForThreePointIntegration);
        double[][] matrixHForFourPointIntegration = getMatrixHForFourPointIntegration(determinant);
        System.out.println("MatrixHForFourPointIntegration: ");
        MatrixService.showTable2D(matrixHForFourPointIntegration);
    }

    private static double[][] getMatrixHForTwoPointIntegration(double determinant) {
        double[][] tableOfKsiIntegral = IntegralFunctions.calculateAndShowFirstArray();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowSecondArray();
        //===================Two point integration==========
        double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GaussianQuadrature.getWeights(2); //{1.0, 1.0}
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

    private static double[][] getMatrixHForThreePointIntegration(double determinant) {

        double[][] tableOfKsiIntegral = IntegralFunctions.calculateAndShowThirdArray();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowFourthArray();
        //===================Three point integration==========
        double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GaussianQuadrature.getWeights(3); //{5.0 / 9.0, 8.0 / 9, 5.0 / 9}
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

    private static double[][] getMatrixHForFourPointIntegration(double determinant) {
        double[][] tableOfKsiIntegral = IntegralFunctions.calculateAndShowFifthArray();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowSixthArray();
        //===================Four point integration==========
        double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GaussianQuadrature.getWeights(4); //{5.0 / 9.0, 8.0 / 9, 5.0 / 9}
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
        //Obliczanie macierzy H dla pierwszego punktu calkowania
        double[][] table1DlaH = new double[4][4];
        double[][] table2DlaH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaH[i][j] = table1DlaPc1[whichPc][i] * table1DlaPc1[whichPc][j];//zmiana dla Hpc2
                table2DlaH[i][j] = table2DlaPc1[whichPc][i] * table2DlaPc1[whichPc][j];
            }
        }
        //dV realizujemy poprzez przemnożenie wyniku przez Jakobian przekształcenia tego punktu całkowania
        double[][] Hpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double value = 30 * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
//                value = BigDecimal.valueOf(value)
//                        .setScale(3, RoundingMode.HALF_UP)
//                        .doubleValue();
                Hpc[i][j] = value;
//                System.out.print(Hpc[i][j] + ", ");
            }
//            System.out.println();
        }

        return Hpc;
    }

    private static double[][] getTableOfEta2IntegralMultipliedByMatrix(double[][] tableOfKsiIntegral, double[][] tableOfEtaIntegral) {
        double[][] table2DlaPc1 = new double[tableOfKsiIntegral.length][4];
        for (int i = 0; i < tableOfKsiIntegral.length; i++) {
            for (int j = 0; j < 4; j++) {
                table2DlaPc1[i][j] = mainMatrix[1][0] * tableOfKsiIntegral[i][j] + mainMatrix[1][1] * tableOfEtaIntegral[i][j];
            }
        }
        return table2DlaPc1;
    }

    private static double[][] getTableOfKsi1IntegralMultipliedByMatrix(double[][] tableOfKsiIntegral, double[][] tableOfEtaIntegral) {
        double[][] table1DlaPc1 = new double[tableOfKsiIntegral.length][4];
        for (int i = 0; i < tableOfKsiIntegral.length; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaPc1[i][j] = mainMatrix[0][0] * tableOfKsiIntegral[i][j] + mainMatrix[0][1] * tableOfEtaIntegral[i][j];
            }
        }
        return table1DlaPc1;
    }






    private static double[][] calculateAndShowSecondArray() {
        //Druga tabela od gory
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }

        //=======================================
        double pcx[] = MatrixService.getPcx();
        double sum1DxDeta = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DxDeta += table4[0][j] * pcx[j];
        }
        System.out.println("Suma1DxDeta: " + sum1DxDeta);
        mainMatrix[1][0] = sum1DxDeta;
        //=========================================
        double pcy[] = MatrixService.getPcy();
        double sum1DyDeta = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DyDeta += table4[0][j] * pcy[j];
        }
        System.out.println("Suma1DyDeta: " + sum1DyDeta);
        mainMatrix[1][1] = sum1DyDeta;
        //=========================================


        System.out.println("SecondArray");
        return table4;
    }

    private static double[][] calculateAndShowFirstArray() throws FileNotFoundException {
        //Pierwsza tabela od gory
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        //======================================
        double pcx[] = MatrixService.getPcx();
        double sum1DxDksi = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DxDksi += table4[0][j] * pcx[j];
        }
        System.out.println("Suma1DxDksi: " + sum1DxDksi);
        mainMatrix[0][0] = sum1DxDksi;
        //========================================
        double pcy[] = MatrixService.getPcy();
        double sum1DyDksi = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DyDksi += table4[0][j] * pcy[j];
        }
        System.out.println("Suma1DyDksi: " + sum1DyDksi);
        mainMatrix[0][1] = sum1DyDksi;
        //========================================


        System.out.println("FirstArray");
        return table4;
    }
}
