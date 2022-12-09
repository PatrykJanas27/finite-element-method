package org.example.lab4;

import static org.example.lab4.MatrixHService.getMatrixHForTwoPointIntegration;

public class Aggregation {
    public static void main(String[] args) {
        double[][] matrixHForTwoPointIntegration = getMatrixHForTwoPointIntegration();
        double[][] aggregationMatrixForTwoPointIntegration = new double[16][16];
        double[] pcx = MatrixService.getPcx();
        double[] pcy = MatrixService.getPcy();

    }
}
