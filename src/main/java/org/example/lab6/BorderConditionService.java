package org.example.lab6;

import org.example.lab1.Grid;
import org.example.lab4.MatrixService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BorderConditionService {
    public static void calculateMatrixHbc(Grid grid) {
        double[][] ksiEta = new double[][]{
                {-(1 / Math.sqrt(3)), -1},
                {(1 / Math.sqrt(3)), -1},

                {1, -(1 / Math.sqrt(3))},
                {1, (1 / Math.sqrt(3))},

                {-(1 / Math.sqrt(3)), 1},
                {(1 / Math.sqrt(3)), 1},

                {-1, -(1 / Math.sqrt(3))},
                {-1, (1 / Math.sqrt(3))},
        };

        double[][] beforeHbc1 = new double[2][4];
        double[][] beforeHbc2 = new double[2][4];
        double[][] beforeHbc3 = new double[2][4];
        double[][] beforeHbc4 = new double[2][4];
        for (int i = 0; i < 4; i++) {
            beforeHbc1[0][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
            beforeHbc1[1][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
            beforeHbc2[0][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);
            beforeHbc2[1][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);

            beforeHbc3[0][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);
            beforeHbc3[1][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
            beforeHbc4[0][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
            beforeHbc4[1][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
        }
        System.out.println("Before Hbc 4: ");
        MatrixService.showTable2D(beforeHbc4);

        double detJ = 0.0125;
        double alfaFactor = 25;
        double[][] pow1 = new double[4][4];
        double[][] pow2 = new double[4][4];
        double[][] pow3 = new double[4][4];
        double[][] pow4 = new double[4][4];
        double[] w = new double[]{1.0, 1.0};

        for (int n = 0; n < 2; n++) { //here is a loop for pc1 and pc2
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    pow1[i][j] += w[n] * alfaFactor * beforeHbc1[n][i] * beforeHbc1[n][j];
                    pow2[i][j] += w[n] * alfaFactor * beforeHbc2[n][i] * beforeHbc2[n][j];
                    pow3[i][j] += w[n] * alfaFactor * beforeHbc3[n][i] * beforeHbc3[n][j];
                    pow4[i][j] += w[n] * alfaFactor * beforeHbc4[n][i] * beforeHbc4[n][j];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pow1[i][j] *= detJ;
                pow2[i][j] *= detJ;
                pow3[i][j] *= detJ;
                pow4[i][j] *= detJ;
            }
        }
        System.out.println("pow1: ");
        MatrixService.showTable2D(pow1);
        System.out.println("pow2: ");
        MatrixService.showTable2D(pow2);
        System.out.println("pow3: ");
        MatrixService.showTable2D(pow3);
        System.out.println("pow4: ");
        MatrixService.showTable2D(pow4);

        //calculating border conditions for elements

//        double[][] sumPow = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                sumPow[i][j] = pow1[i][j] + pow2[i][j] + pow3[i][j] + pow4[i][j];
//            }
//        }
//
//        System.out.println("sumPow: ");
//        MatrixService.showTable2D(sumPow);
    }

    public static Double geometricModelsN(int whichOneFrom1To4, double ksi, double eta) {
        List<BiFunction<Double, Double, Double>> geometricModels = new ArrayList<>() {
            {
                add((ksi, eta) -> 0.25 * (1 - ksi) * (1 - eta));
                add((ksi, eta) -> 0.25 * (1 + ksi) * (1 - eta));
                add((ksi, eta) -> 0.25 * (1 + ksi) * (1 + eta));
                add((ksi, eta) -> 0.25 * (1 - ksi) * (1 + eta));
            }
        };
        return geometricModels.get(whichOneFrom1To4).apply(ksi, eta);
    }

    public static TriFunction<Double, Double, Double, Double> getBorderConditionTriFunction() {
        TriFunction<Double, Double, Double, Double> heatBorderCondition = new TriFunction<Double, Double, Double, Double>() {
            @Override
            public Double apply(Double alfaFactor, Double temperature, Double temperatureAmb) {
                return alfaFactor * (temperature - temperatureAmb);
            }
        };
        return heatBorderCondition;
    }
}
