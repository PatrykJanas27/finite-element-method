package org.example.lab6;


/**
 * ***************** source of this code for GaussianElimination ************************
 * https://introcs.cs.princeton.edu/java/95linear/GaussianElimination.java.html
 */
public class GaussianEliminationService {
    private static final double EPSILON = 1e-10;

    public static double[] findSolutionForSystemOfEquations(double[][] globalAggregationHplusHBC, double[] globalAggregationVectorP) {
        double[][] A = new double[16][16];
        double[] b = new double[16];
        for (int i = 0; i < globalAggregationHplusHBC.length; i++) {
            for (int j = 0; j < globalAggregationHplusHBC[i].length; j++) {
                A[i][j] = globalAggregationHplusHBC[i][j];
            }
            b[i] = globalAggregationVectorP[i];
        }
        return lsolve(A, b);
    }

    // Gaussian elimination with partial pivoting
    public static double[] lsolve(double[][] A, double[] b) {
        int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
}
