package org.example.lab4;

import static org.example.lab4.MatrixHService.*;

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
}
