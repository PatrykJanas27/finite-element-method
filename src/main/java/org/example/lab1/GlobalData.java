package org.example.lab1;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Setter
@Getter
public class GlobalData {
    public static int simulationTime;
    public static int simulationStepTime;
    public static int conductivity;
    public static int alfa;
    public static int tot;
    public static int initialTemp;
    public static int density;
    public static int specificHeat;

    @Override
    public String toString() {
        return "org.example.GlobalData{" +
                "simulationTime=" + simulationTime +
                ", simulationStepTime=" + simulationStepTime +
                ", conductivity=" + conductivity +
                ", alfa=" + alfa +
                ", tot=" + tot +
                ", initialTemp=" + initialTemp +
                ", density=" + density +
                ", specificHeat=" + specificHeat +
                '}';
    }

    public static List<Double> getPcList(int numberOfNodes) {
        List<Double> pc = new ArrayList<>();
        switch (numberOfNodes) {
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
            default -> throw new IllegalArgumentException("There is not defined that numberOfNodes");
        }
        return pc;
    }

    public static List<Double> getWeightsList(int numberOfNodes) {
        List<Double> w = new ArrayList<>();
        switch (numberOfNodes) {
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
            default -> throw new IllegalArgumentException("There is not defined that numberOfNodes");
        }
        return w;
    }

    public static double[] getPcArray(int numberOfNodes) {
        switch (numberOfNodes) {
            case 2 -> {
                return new double[]{
                        (-1 / sqrt(3)), (-1 / sqrt(3)),
                        (1 / sqrt(3)), (1 / sqrt(3))
                };
            }
            case 3 -> {
                return new double[]{
                        -sqrt(3.0 / 5), -sqrt(3.0 / 5), -sqrt(3.0 / 5),
                        0, 0, 0,
                        sqrt(3.0 / 5), sqrt(3.0 / 5), sqrt(3.0 / 5)
                };
            }
            case 4 -> {
                return new double[]{
                        -0.861136, -0.861136, -0.861136, -0.861136,
                        -0.339981, -0.339981, -0.339981, -0.339981,
                        0.339981, 0.339981, 0.339981, 0.339981,
                        0.861136, 0.861136, 0.861136, 0.861136
                };
            }
            case 5 -> {
                return new double[]{
                        -0.906180, -0.538469,
                        0.0,
                        0.538469, 0.906180
                };
            }
            default -> throw new IllegalArgumentException("There is not defined that numberOfNodes");
        }
    }

    public static double[] getWeightsArray(int numberOfPoints) {
        switch (numberOfPoints) {
            case 2 -> {
                return new double[]{1.0, 1.0};
            }
            case 3 -> {
                return new double[]{5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
            }
            case 4 -> {
                return new double[]{0.347855, 0.652145, 0.652145, 0.347855};
            }
            case 5 -> {
                return new double[]{0.236927, 0.478629, 0.568889, 0.478629, 0.236927};
            }
            default -> throw new IllegalArgumentException("There is not defined that numberOfNodes");
        }
    }

    public static List<Function<Double, Double>> getFunctionsForKsi() {
        return new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
    }

    public static List<Function<Double, Double>> getFunctionsForEta() {
        return new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
    }


}
