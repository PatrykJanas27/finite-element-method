package org.example.lab4;

public class GaussianQuadrature {

    public static double[] getWeights(int numberOfPoints) {
        switch (numberOfPoints) {
            case 2 -> {
                return new double[]{1.0, 1.0};
            }
            case 3 -> {
                return new double[]{5.0/9.0, 8.0/9.0, 5.0/9.0 };
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
}
