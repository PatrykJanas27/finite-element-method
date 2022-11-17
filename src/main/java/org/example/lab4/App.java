package org.example.lab4;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class App {
    public static double[][] dlaPc1 = new double[2][2];

    public static void main(String[] args) throws FileNotFoundException {
        double[][] firstTable = calculateAndShowFirstArray();
        double[][] secondTable = calculateAndShowSecondArray();
        calculateAndShowThirdArray();
        calculateAndShowFourthArray();


        //det[j] dla pc1
        System.out.println("dlaPc1: ");
        double detJ = dlaPc1[0][0] * dlaPc1[1][1] - dlaPc1[0][1] * dlaPc1[1][0];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(dlaPc1[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("detJ: " + detJ);
        System.out.println("1/detJ: " + 1.0 / detJ);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                dlaPc1[i][j] = dlaPc1[i][j] * (1.0 / detJ);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(dlaPc1[i][j] + " ");
            }
            System.out.println();
        }

//        //=======dN1/dX
//        double dN1dX = dlaPc1[0][0] * firstTable[0][0] + dlaPc1[0][1] * secondTable[0][0];
//        System.out.println("dN1/dX: " + dN1dX);
//        //=========dN2/dX
//        double dN2dX = dlaPc1[0][0] * firstTable[0][1] + dlaPc1[0][1] * secondTable[0][1];
//        System.out.println("dN2/dX: " + dN2dX);
//        //=========dN3/dX
//        double dN3dX = dlaPc1[0][0] * firstTable[0][2] + dlaPc1[0][1] * secondTable[0][2];
//        System.out.println("dN3/dX: " + dN3dX);
//        //=========dN4/dX
//        double dN4dX = dlaPc1[0][0] * firstTable[0][3] + dlaPc1[0][1] * secondTable[0][3];
//        System.out.println("dN4/dX: " + dN4dX);
//        //=======dlaPc2dN1/dX
//        double dlaPc2dN1dX = dlaPc1[0][0] * firstTable[1][0] + dlaPc1[0][1] * secondTable[1][0];
//        System.out.println("dlaPc2dN1dX: " + dlaPc2dN1dX);
//        //=======dlaPc2dN1/dX
//        double dlaPc2dN2dX = dlaPc1[0][0] * firstTable[1][1] + dlaPc1[0][1] * secondTable[1][1];
//        System.out.println("dlaPc2dN2dX: " + dlaPc2dN2dX);

//        dlaHpc1(firstTable, secondTable, detJ);

        double[][] table1DlaPc1 = getTable1DlaPc1(firstTable, secondTable);
        double[][] table2DlaPc1 = getTable2DlaPc1(firstTable, secondTable);
//        double[][] Hpc2 = dlaHpc2(table1DlaPc1, table2DlaPc1, detJ, 2);
//        double[][] Hpc3 = dlaHpc2(table1DlaPc1, table2DlaPc1, detJ, 3);
//        System.out.println("Hpc2: ");
//        showHpc(Hpc2);


        double[][] w = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        for (int i = 0; i < 4; i++) {
            double[][] Hpc = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, i);
            System.out.println("Hpc" + (i + 1) + ": ");
            showHpc(Hpc);

        }
        double H[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    H[i][j] += calculateHpc(table1DlaPc1, table2DlaPc1, detJ, k)[i][j];
                }
            }
        }
        System.out.println("H:");
        showHpc(H);


    }

    private static void showHpc(double[][] hpc2) {
        for (int i = 0; i < hpc2.length; i++) {
            for (int j = 0; j < hpc2.length; j++) {
                System.out.print(hpc2[i][j] + " ");
            }
            System.out.println();
        }
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

    private static double[][] getTable2DlaPc1(double[][] firstTable, double[][] secondTable) {
//        System.out.println("table2DlaPc1: ");
        double[][] table2DlaPc1 = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table2DlaPc1[i][j] = dlaPc1[1][0] * firstTable[i][j] + dlaPc1[1][1] * secondTable[i][j];
//                System.out.print(table2DlaPc1[i][j] + ", ");
            }
//            System.out.println();
        }
        return table2DlaPc1;
    }

    private static double[][] getTable1DlaPc1(double[][] firstTable, double[][] secondTable) {
//        System.out.println("======dlaHpc2=====");
//        System.out.println("table1DlaPc1: ");
        double[][] table1DlaPc1 = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table1DlaPc1[i][j] = dlaPc1[0][0] * firstTable[i][j] + dlaPc1[0][1] * secondTable[i][j];
//                System.out.print(table1DlaPc1[i][j] + ", ");
            }
//            System.out.println();
        }
        return table1DlaPc1;
    }


//    private static void dlaHpc1(double[][] firstTable, double[][] secondTable, double detJ) {
//        System.out.println("table1DlaPc1: ");
//        double[][] table1DlaPc1 = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table1DlaPc1[i][j] = dlaPc1[0][0] * firstTable[i][j] + dlaPc1[0][1] * secondTable[i][j];
//                System.out.print(table1DlaPc1[i][j] + ", ");
//            }
//            System.out.println();
//        }
//
//        System.out.println("table2DlaPc1: ");
//        double[][] table2DlaPc1 = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table2DlaPc1[i][j] = dlaPc1[1][0] * firstTable[i][j] + dlaPc1[1][1] * secondTable[i][j];
//                System.out.print(table2DlaPc1[i][j] + ", ");
//            }
//            System.out.println();
//        }
//
//        //Obliczanie macierzy H dla pierwszego punktu calkowania
//        System.out.println("table1DlaH: ");
//        double[][] table1DlaH = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table1DlaH[i][j] = table1DlaPc1[0][i] *  table1DlaPc1[0][j];
//                System.out.print(table1DlaH[i][j] + ", ");
//            }
//            System.out.println();
//        }
//
//        System.out.println("table2DlaH: ");
//        double[][] table2DlaH = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                table2DlaH[i][j] = table2DlaPc1[0][i] *  table2DlaPc1[0][j];
//                System.out.print(table2DlaH[i][j] + ", ");
//            }
//            System.out.println();
//        }
//
//        //dV realizujemy poprzez przemnożenie wyniku przez Jakobian przekształcenia tego punktu całkowania
//        System.out.println("Hpc1: ");
//        double[][] Hpc1 = new double[4][4];
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                Hpc1[i][j] = 30 * (table1DlaH[i][j] + table2DlaH[i][j]) * detJ;
//                System.out.print(Hpc1[i][j] + ", ");
//            }
//            System.out.println();
//        }
//    }

    private static void calculateAndShowFourthArray() {
        //Czwarta tabela od gory
        double[] ksi = new double[]{
                -sqrt(3.0 / 5), 0, sqrt(3.0 / 5), -sqrt(3.0 / 5), 0, sqrt(3.0 / 5), -sqrt(3.0 / 5), 0, sqrt(3.0 / 5)
        };
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("FourthArray");
        showArray(ksi, table4, 9, 4);
    }

    private static void calculateAndShowThirdArray() {
        //Trzecia tabela od gory
        double[] eta = new double[]{
                -sqrt(3.0 / 5), -sqrt(3.0 / 5), -sqrt(3.0 / 5),
                0, 0, 0,
                sqrt(3.0 / 5), sqrt(3.0 / 5), sqrt(3.0 / 5)
        };
        double[][] table4 = new double[9][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("ThirdArray");
        showArray(eta, table4, 9, 4);
    }

    private static double[][] calculateAndShowSecondArray() {
        //Druga tabela od gory
        double[] ksi = new double[]{(-1 / sqrt(3)), (1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }

        //=======================================
        double pcx[] = new double[]{0, 0.025, 0.025, 0};
        double sum1DxDeta = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DxDeta += table4[0][j] * pcx[j];
        }
        System.out.println("Suma1DxDeta: " + sum1DxDeta);
        dlaPc1[1][0] = sum1DxDeta;
        //=========================================
        double pcy[] = new double[]{0, 0, 0.025, 0.025};
        double sum1DyDeta = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DyDeta += table4[0][j] * pcy[j];
        }
        System.out.println("Suma1DyDeta: " + sum1DyDeta);
        dlaPc1[1][1] = sum1DyDeta;
        //=========================================


        System.out.println("SecondArray");
        showArray(ksi, table4, 4, 4);
        return table4;
    }

    private static double[][] calculateAndShowFirstArray() throws FileNotFoundException {
        //Pierwsza tabela od gory
        double[] eta = new double[]{(-1 / sqrt(3)), (-1 / sqrt(3)), (1 / sqrt(3)), (1 / sqrt(3))};
        double[][] table4 = new double[4][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(ksi -> 0.25 * (ksi - 1));
                add(ksi -> 0.25 * (1 - ksi));
                add(ksi -> 0.25 * (ksi + 1));
                add(ksi -> 0.25 * (-ksi - 1));
            }
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
            }
        }
        //======================================
        double pcx[] = new double[]{0, 0.025, 0.025, 0};
        double sum1DxDksi = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DxDksi += table4[0][j] * pcx[j];
        }
        System.out.println("Suma1DxDksi: " + sum1DxDksi);
        dlaPc1[0][0] = sum1DxDksi;
        //========================================
        double pcy[] = new double[]{0, 0, 0.025, 0.025};
        double sum1DyDksi = 0;
        for (int j = 0; j < table4.length; j++) {
            sum1DyDksi += table4[0][j] * pcy[j];
        }
        System.out.println("Suma1DyDksi: " + sum1DyDksi);
        dlaPc1[0][1] = sum1DyDksi;
        //========================================


//        Grid grid = org.example.lab1.App.readFile("src/main/resources/lab1/Test1_4_4.txt");
//        List<Node> nd = grid.getND();
//        nd.forEach(System.out::println);


        System.out.println("FirstArray");
        showArray(eta, table4, 4, 4);
        return table4;
    }

//    private static void showSums(double[] eta, double[][] table) {
//        double[] sums = new double[table.length];
//        for (int i = 0; i < table.length; i++) {
//            for (int j = 0; j < table.length; j++) {
//                sums[i] = table[i][j] * eta[i];
//            }
//        }
//        for (int i = 0; i < sums.length; i++) {
//            System.out.println("sum" + i + ": " + sums[i]);
//        }
//    }

    private static void showArray(double[] etaOrKsi, double[][] table4, int maxI, int maxJ) {
        System.out.print("\t\t[eta]\t\t\t\t\t[dN1/dksi]\t\t\t\tdN2/dksi\t\t\t\tdN3/dksi\t\t\t\tdN4/dksi\n");
        for (int i = 0; i < maxI; i++) {
            System.out.print("PC" + i + "\t");
            System.out.print("[" + etaOrKsi[i] + "],\t");
            for (int j = 0; j < maxJ; j++) {
                System.out.print("[" + table4[i][j] + "], ");
            }
            System.out.print("\n");
        }
    }
}
