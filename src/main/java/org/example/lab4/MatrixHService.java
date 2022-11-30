package org.example.lab4;

public class MatrixHService {

    public static void getMatrixHForThreePointIntegration(){

    }


    private static double[][] calculateHpc(double[][] table1DlaPc1, double[][] table2DlaPc1, double detJ, int whichPc) {
        //Obliczanie macierzy H dla pierwszego punktu calkowania
        double[][] table1DlaH = new double[4][4];
        double[][] table2DlaH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaH[i][j] = table1DlaPc1[whichPc][i] * table1DlaPc1[whichPc][j];//zmiana dla Hpc2
                table2DlaH[i][j] = table2DlaPc1[whichPc][i] * table2DlaPc1[whichPc][j];
            }
        }
        //dV realizujemy poprzez przemnożenie wyniku przez Jakobian przekształcenia tego punktu całkowania
        double[][] Hpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Double value = 30 * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
//                value = BigDecimal.valueOf(value)
//                        .setScale(3, RoundingMode.HALF_UP)
//                        .doubleValue();
                Hpc[i][j] = value;
//                System.out.print(Hpc[i][j] + ", ");
            }
//            System.out.println();
        }

        return Hpc;
    }


}
