package org.example.lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class IntegralFunctions {
    public static double[][] calculateAndShowSixthArray() {
        //Czwarta tabela od gory
        double[] ksi = new double[]{
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136
        };
        double[][] table4 = new double[16][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("SixthArray");
        return table4;
    }

    public static double[][] calculateAndShowFifthArray() {
        //piąta tabela od góry
        double[] eta = new double[]{
                -0.861136, -0.861136, -0.861136, -0.861136,
                -0.339981, -0.339981, -0.339981, -0.339981,
                0.339981, 0.339981, 0.339981, 0.339981,
                0.861136, 0.861136, 0.861136, 0.861136
        };
        double[][] table4 = new double[16][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("FifthArray");
        return table4;
    }

    public static double[][] calculateAndShowFourthArray() {
        //Czwarta tabela od gory
        double[] ksi = new double[]{
                -sqrt(3.0 / 5), 0, sqrt(3.0 / 5), -sqrt(3.0 / 5), 0, sqrt(3.0 / 5), -sqrt(3.0 / 5), 0, sqrt(3.0 / 5)
        };
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("FourthArray");
        return table4;
    }

    public static double[][] calculateAndShowThirdArray() {
        //Trzecia tabela od gory
        double[] eta = new double[]{
                -sqrt(3.0 / 5), -sqrt(3.0 / 5), -sqrt(3.0 / 5),
                0, 0, 0,
                sqrt(3.0 / 5), sqrt(3.0 / 5), sqrt(3.0 / 5)
        };
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("ThirdArray");
        return table4;
    }

    public static double[][] calculateAndShowSecondArray() {
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
        System.out.println("SecondArray");
        return table4;
    }

    public static double[][] calculateAndShowFirstArray() {
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
        System.out.println("FirstArray");
        return table4;
    }


}
