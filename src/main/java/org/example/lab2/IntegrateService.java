package org.example.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.pow;

public class IntegrateService {

    public static double calculate(Function<Double, Double> uniFunction, int n) {
        List<Double> pc = pc(n);
        List<Double> w = w(n);
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += uniFunction.apply(pc.get(i)) * w.get(i);
        }
        return result;
    }

    public static double calculate(BiFunction<Double, Double, Double> biFunction, int n) {
        List<Double> pc = pc(n);
        List<Double> w = w(n);
        double result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result += biFunction.apply(pc.get(i), pc.get(j)) * w.get(i) * w.get(j);
            }
        }
        return result;
    }

    public static List<Double> pc(int numberOfK) {
        List<Double> pc = new ArrayList<>();
        switch (numberOfK) {
            case 2 -> {
                pc.add(-1 / (pow(3, 0.5)));
                pc.add(1 / (pow(3, 0.5)));
            }
            case 3 -> {
                pc.add(-pow(3.0 / 5.0, 0.5));
                pc.add(0.0);
                pc.add(pow(3.0 / 5.0, 0.5));
            }
            case 4 -> {
                pc.add(-0.861136);
                pc.add(-0.339981);
                pc.add(0.339981);
                pc.add(0.861136);
            }
            case 5 -> {
                pc.add(-0.906180);
                pc.add(-0.538469);
                pc.add(0.0);
                pc.add(0.538469);
                pc.add(0.906180);
            }
            default -> throw new IllegalArgumentException("There is not defined that number of 'K'");
        }
        return pc;
    }

    public static List<Double> w(int numberOfK) {
        List<Double> w = new ArrayList<>();
        switch (numberOfK) {
            case 2 -> {
                w.add(1.0);
                w.add(1.0);
            }
            case 3 -> {
                w.add(5.0 / 9.0);
                w.add(8.0 / 9.0);
                w.add(5.0 / 9.0);
            }
            case 4 -> {
                w.add(0.347855);
                w.add(0.652145);
                w.add(0.652145);
                w.add(0.347855);
            }
            case 5 -> {
                w.add(0.236927);
                w.add(0.478629);
                w.add(0.568889);
                w.add(0.478629);
                w.add(0.236927);
            }
            default -> throw new IllegalArgumentException("There is not defined that number of 'K'");
        }
        return w;
    }

}



