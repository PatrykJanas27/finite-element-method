package org.example.lab4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class MatrixService {
    public static void showTable2D(double[][] table2D) {
        for (int i = 0; i < table2D.length; i++) {
            for (int j = 0; j < table2D[i].length; j++) {
//                System.out.printf("|\t%-22f|", table2D[i][j]);
                System.out.printf("|\t%-10.3f|", table2D[i][j]);
            }
            System.out.println();
        }
    }

    public static void showTable2Dshort(double[][] table2D) {
        for (int i = 0; i < table2D.length; i++) {
            for (int j = 0; j < table2D[i].length; j++) {
//                System.out.printf("%-10f|", table2D[i][j]);
                System.out.printf("\t%-6.2f|", table2D[i][j]);
            }
            System.out.println();
        }
    }

    public static void showTable1D(double[] table1D) {
        Arrays.stream(table1D).forEach(x-> System.out.printf(" |%-6.2f|", x));
        System.out.println();
    }

    public static double[] getPcx() {
        return new double[]{0, 0.025, 0.025, 0};
    }

    public static double[] getPcy() {
        return new double[]{0, 0, 0.025, 0.025};
    }


    public static double[][] getMainMatrix() {
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table1 = new double[4][4];
        double[][] table2 = new double[4][4];
        List<Function<Double, Double>> functions1 = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        List<Function<Double, Double>> functions2 = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
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

    public static double[][] getMainMatrix(double[] pcx, double[] pcy) {
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table1 = new double[4][4];
        double[][] table2 = new double[4][4];
        List<Function<Double, Double>> functions1 = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        List<Function<Double, Double>> functions2 = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1[i][j] = functions1.get(j).apply(eta[i]);
                table2[i][j] = functions2.get(j).apply(ksi[i]);
            }
        }

        double[][] mainMatrix = new double[2][2];
        for (int j = 0; j < 4; j++) {
            mainMatrix[0][0] += table1[0][j] * pcx[j];
            mainMatrix[0][1] += table1[0][j] * pcy[j];
            mainMatrix[1][0] += table2[0][j] * pcx[j];
            mainMatrix[1][1] += table2[0][j] * pcy[j];
        }
        return mainMatrix;
    }

}
