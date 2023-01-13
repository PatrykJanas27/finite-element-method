package org.example.lab3;

import org.example.lab1.GlobalData;

import java.util.List;

@Deprecated
public class ServicePc {

    public static List<Double> calculatePc(double x1, double x2, int numberOfNodes) {
        return GlobalData.getPcList(numberOfNodes)
                .stream()
                .map(pc -> calculateN1(pc) * x1 + calculateN2(pc) * x2)
                .toList();
    }

    public static double calculateN1(double x) {
        return (1 - x) / 2;
    }

    public static double calculateN2(double x) {
        return (x + 1) / 2;
    }
}
