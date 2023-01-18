package org.example.data;

import java.util.Arrays;

public class MatrixService {
    public static void showTable2D(double[][] table2D) {
        for (int i = 0; i < table2D.length; i++) {
            for (int j = 0; j < table2D[i].length; j++) {
//                System.out.printf("|\t%-22f|", table2D[i][j]);
                System.out.printf("|\t%-10.3f|", table2D[i][j]);
            }
            System.out.println();
        }
    }

    public static void showTable2Dshort(double[][] table2D) {
        for (int i = 0; i < table2D.length; i++) {
            for (int j = 0; j < table2D[i].length; j++) {
//                System.out.printf("%-10f|", table2D[i][j]);
                System.out.printf("\t%-6.2f|", table2D[i][j]);
            }
            System.out.println();
        }
    }

    public static void showTable1D(double[] table1D) {
        Arrays.stream(table1D).forEach(x -> System.out.printf(" |%-6.2f|", x));
        System.out.println();
    }

}
