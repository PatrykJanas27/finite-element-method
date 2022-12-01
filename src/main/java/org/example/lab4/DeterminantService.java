package org.example.lab4;

import org.example.lab4.exceptions.MatrixException;

public class DeterminantService {

    public static double[][] getMatrix2x2MultipliedBy1DividedByDeterminant(double[][] matrix2x2, double determinant) {
        checkIfMatrix2x2(matrix2x2);
        //Matrix multiply by 1/determinant
        //determinant = 0.00015625
        //1/determinant = 6400
        //6400 * [0,0125    0  ]
        //       [  0    0,0125]
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrix2x2[i][j] = (1.0 / determinant) * matrix2x2[i][j];
            }
        }
        //Result matrix
        //[80.0   0.0]
        //[ 0.0  80.0]
        return matrix2x2;
    }

    public static double getDeterminant2x2(double[][] matrix2x2) {
        checkIfMatrix2x2(matrix2x2);
        return matrix2x2[0][0] * matrix2x2[1][1] - matrix2x2[0][1] * matrix2x2[1][0];
    }

    private static void checkIfMatrix2x2(double[][] matrix2x2) {
        if (matrix2x2.length != 2 || matrix2x2[0].length != 2) {
            throw new MatrixException(String.format("Matrix has to be 2 dimensional, now is %dx%d",
                    matrix2x2.length, matrix2x2[0].length));
        }
    }


}
