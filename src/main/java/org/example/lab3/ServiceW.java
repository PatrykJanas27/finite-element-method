package org.example.lab3;

import org.example.lab2.IntegrateService;

import java.util.ArrayList;
import java.util.List;

public class ServiceW {
    public static List<Double> calculateW(List<Double> w, double x1, double x2) {
        List<Double> resultW = new ArrayList<>();
        for (Double aDouble : w) {
            resultW.add(aDouble * geometricModel(x1, x2)); //important
        }
        return resultW;
    }

    public static List<Double> calculateW(double x1, double x2, int numberOfNodes) {
        List<Double> w = IntegrateService.w(numberOfNodes);
        List<Double> resultW = new ArrayList<>();
        for (Double aDouble : w) {
            resultW.add(aDouble * geometricModel(x1, x2));
        }
        return resultW;
    }

    public static double geometricModel(double x1, double x2) {
        return ((x1 + x2) / 2) - x1;
    }
}
