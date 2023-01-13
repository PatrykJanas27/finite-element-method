package org.example.lab3;

import org.example.lab1.GlobalData;

import java.util.List;

@Deprecated
public class ServiceW {

    public static List<Double> calculateW(double x1, double x2, int numberOfNodes) {
        return GlobalData.getWeightsList(numberOfNodes)
                .stream()
                .map(a -> a * geometricModel(x1, x2))
                .toList();
    }

    public static double geometricModel(double x1, double x2) {
        return ((x1 + x2) / 2) - x1;
    }
}
