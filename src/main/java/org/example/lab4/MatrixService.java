package org.example.lab4;

public class MatrixService {
    public static void showTable2D(double[][] table2D){
        for (int i = 0; i < table2D.length; i++) {
            for (int j = 0; j < table2D[i].length; j++) {
                System.out.print(String.format("|\t%-22f|", table2D[i][j]));
            }
            System.out.println();
        }
    }

    public static double[] getPcx(){
        return new double[]{0, 0.025, 0.025, 0};
    }

    public static double[] getPcy(){
        return new double[]{0, 0, 0.025, 0.025};
    }

}
