package org.example.lab4;

import org.example.lab1.GlobalData;

public class MatrixHService {

    public static double[][] getMatrixHForTwoPointIntegrationWithGlobalData(double[][] mainMatrix) {
        //===================Calculation for determinant===========
        double determinant = DeterminantService.getDeterminant2x2(mainMatrix); // 0,00015625
        mainMatrix = DeterminantService.getMatrix2x2MultipliedBy1DividedByDeterminant(mainMatrix, determinant);

        double[][] tableOfKsiIntegral = IntegralFunctions.getTableForDnDividedByDKsi();
        double[][] tableOfEtaIntegral = IntegralFunctions.calculateAndShowSecondArray();
        //===================Two point integration==========
        double[][] tableOfKsiIntegralByM = getTableOfKsi1IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
        double[][] tableOfEtaIntegralByM = getTableOfEta2IntegralMultipliedByMatrix(mainMatrix, tableOfKsiIntegral, tableOfEtaIntegral);
//        System.out.println("tableOfKsiIntegralByM 1: ");
//        MatrixService.showTable2D(tableOfKsiIntegralByM);
//        System.out.println("tableOfEtaIntegralByM 2:");
//        MatrixService.showTable2D(tableOfEtaIntegralByM);
        //===================WeightsOfPoints=======
        double[] weightsOfPoints = GlobalData.getWeightsArray(2); //{1.0, 1.0}
        //===================Calculating tables of H for tree point integration (there will be 9 tables)==========
        double[][] Hpc1 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 0);
        double[][] Hpc2 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 1);
        double[][] Hpc3 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 2);
        double[][] Hpc4 = calculateHpcWithGlobalData(tableOfKsiIntegralByM, tableOfEtaIntegralByM, determinant, 3);
        System.out.println("Matrix H1");
        MatrixService.showTable2Dshort(Hpc1);
        System.out.println("Matrix H2");
        MatrixService.showTable2Dshort(Hpc2);
        System.out.println("Matrix H3");
        MatrixService.showTable2Dshort(Hpc3);
        System.out.println("Matrix H4");
        MatrixService.showTable2Dshort(Hpc4);
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



    private static double[][] calculateHpcWithGlobalData(double[][] table1DlaPc1, double[][] table2DlaPc1, double detJ, int whichPc) {
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
                Hpc[i][j] = GlobalData.conductivity * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ; //TODO read from file conductivity
            }
        }
        return Hpc;
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
