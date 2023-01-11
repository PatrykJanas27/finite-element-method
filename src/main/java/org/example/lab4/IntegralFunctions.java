package org.example.lab4;

import org.example.lab1.GlobalData;

import java.util.List;
import java.util.function.Function;

public class IntegralFunctions {
    public static double[][] getTableForDnDividedByDEta(int numberOfNodes) {
        double[] ksi = GlobalData.getPcArray(numberOfNodes);
        double[][] table4 = new double[ksi.length][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctions_dNdEta();
        for (int i = 0; i < ksi.length; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]);
            }
        }
        return table4;
    }

    public static double[][] getTableForDnDividedByDKsi(int numberOfNodes) {
        double[] eta = GlobalData.getPcArray(numberOfNodes);
        double[][] table4 = new double[eta.length][4];
        List<Function<Double, Double>> functions = GlobalData.getFunctions_dNdKsi();
        for (int i = 0; i < eta.length; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]);
            }
        }
        return table4;
    }


}
