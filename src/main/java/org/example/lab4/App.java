package org.example.lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class App {
    public static void main(String[] args) {
        calculateAndShowFirstArray();
        calculateAndShowSecondArray();
        calculateAndShowThirdArray();
        calculateAndShowFourthArray();
    }

    private static void calculateAndShowFourthArray() {
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
        showArray(ksi, table4, 9, 4);
    }

    private static void calculateAndShowThirdArray() {
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
        showArray(eta, table4, 9, 4);
    }

    private static void calculateAndShowSecondArray() {
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
        showArray(ksi, table4, 4, 4);
    }

    private static void calculateAndShowFirstArray() {
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
        showArray(eta, table4, 4, 4);
    }

    private static void showArray(double[] etaOrKsi, double[][] table4, int maxI, int maxJ) {
        System.out.print("\t\t[eta]\t\t\t\t\t[dN1/dksi]\t\t\t\tdN2/dksi\t\t\t\tdN3/dksi\t\t\t\tdN4/dksi\n");
        for (int i = 0; i < maxI; i++) {
            System.out.print("PC" + i + "\t");
            System.out.print("[" + etaOrKsi[i] + "],\t");
            for (int j = 0; j < maxJ; j++) {
                System.out.print("[" + table4[i][j] + "], ");
            }
            System.out.print("\n");
        }
    }
}
