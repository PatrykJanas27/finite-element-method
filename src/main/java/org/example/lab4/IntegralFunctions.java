package org.example.lab4;

import org.example.lab1.GlobalData;

import java.util.List;
import java.util.function.Function;

public class IntegralFunctions {
    public static double[][] calculateAndShowSixthArray() {
        //Czwarta tabela od gory
        double[] ksi = GlobalData.getPcArray(4);
        double[][] table4 = new double[16][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForEta();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }

    public static double[][] calculateAndShowFifthArray() {
        //piąta tabela od góry
        double[] eta = GlobalData.getPcArray(4);
        double[][] table4 = new double[16][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForKsi();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }

    public static double[][] calculateAndShowFourthArray() {
        //Czwarta tabela od gory
        double[] ksi = GlobalData.getPcArray(3);
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForEta();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }

    public static double[][] calculateAndShowThirdArray() {
        //Trzecia tabela od gory
        double[] eta = GlobalData.getPcArray(3);
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForKsi();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }

    public static double[][] calculateAndShowSecondArray() {
        //Druga tabela od gory
        double[] ksi = GlobalData.getPcArray(2);
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForEta();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }

    public static double[][] getTableForDnDividedByDKsi() {
        //Pierwsza tabela od gory
        double[] eta = GlobalData.getPcArray(2);
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctionsForKsi();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        return table4;
    }






}
