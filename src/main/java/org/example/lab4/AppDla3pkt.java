package org.example.lab4;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public class AppDla3pkt {
    public static double[][] dlaPc1 = new double[2][2];

    public static void main(String[] args) throws FileNotFoundException {
        double[][] firstTable = calculateAndShowFirstArray();
        double[][] secondTable = calculateAndShowSecondArray();
        double[][] thirdTable = calculateAndShowThirdArray();
        double[][] fourthTable = calculateAndShowFourthArray();


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
        //80.0 0.0
        //0.0 80.0
        ////===================tutaj dla 3 punktowego teraz

        double[][] table1DlaPc1 = getTable1DlaPc1(thirdTable, fourthTable);
        double[][] table2DlaPc1 = getTable2DlaPc1(thirdTable, fourthTable);

        System.out.println("table 1");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(table1DlaPc1[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("table 2");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(table2DlaPc1[i][j] + " ");
            }
            System.out.println();
        }


        double[] w = new double[]{5.0 / 9.0, 8.0 / 9, 5.0 / 9};
        //Obliczanie macierzy H dla pierwszego punktu całkowania
        for (int i = 0; i < 9; i++) {
            double[][] Hpc = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, i);
            System.out.println("Hpc" + (i + 1) + ": ");
            showHpc(Hpc);
        }

        double[][] Hpc1 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 0);
        double[][] Hpc2 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 1);
        double[][] Hpc3 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 2);
        double[][] Hpc4 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 3);
        double[][] Hpc5 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 4);
        double[][] Hpc6 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 5);
        double[][] Hpc7 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 6);
        double[][] Hpc8 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 7);
        double[][] Hpc9 = calculateHpc(table1DlaPc1, table2DlaPc1, detJ, 8);

        //suma dla HPC
        double[][] sumaHpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sumaHpc[i][j] = Hpc1[i][j] * w[0] * w[0]
                        + Hpc2[i][j] * w[0] * w[1]
                        + Hpc3[i][j] * w[0] * w[2]
                        + Hpc4[i][j] * w[1] * w[0]
                        + Hpc5[i][j] * w[1] * w[1]
                        + Hpc6[i][j] * w[1] * w[2]
                        + Hpc7[i][j] * w[2] * w[0]
                        + Hpc8[i][j] * w[2] * w[1]
                        + Hpc9[i][j] * w[2] * w[2];

            }
        }
        System.out.println("Suma Hpc: ");
        showHpc(sumaHpc);

//            double H[][] = new double[4][4];
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    for (int k = 0; k < 9; k++) {
//                        H[i][j] += calculateHpc(table1DlaPc1, table2DlaPc1, detJ, k)[i][j];
//                    }
//                }
//            }
//            System.out.println("H:");
//            showHpc(H);
        dla4Punktowego(detJ);

    }

    private static void dla4Punktowego(double detJ) {
        double[][] fifthTable = calculateAndShowFifthArray();
        double[][] sixthTable = calculateAndShowSixthArray();
        double[][] dlaFifthTable = getTable1DlaPc1(fifthTable, sixthTable);
        double[][] dlaSixthTable = getTable2DlaPc1(fifthTable, sixthTable);

        double[] wDla4Punktowego = new double[]{0.347855, 0.652145, 0.652145, 0.347855};
        //Obliczanie macierzy H dla pierwszego punktu całkowania
        for (int i = 0; i < 16; i++) {
            double[][] Hpc = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, i);
            System.out.println("Hpc" + (i + 1) + ": ");
            showHpc(Hpc);
        }

        double[][] Hpc1 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 0);
        double[][] Hpc2 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 1);
        double[][] Hpc3 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 2);
        double[][] Hpc4 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 3);
        double[][] Hpc5 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 4);
        double[][] Hpc6 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 5);
        double[][] Hpc7 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 6);
        double[][] Hpc8 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 7);
        double[][] Hpc9 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 8);
        double[][] Hpc10 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 9);
        double[][] Hpc11 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 10);
        double[][] Hpc12 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 11);
        double[][] Hpc13 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 12);
        double[][] Hpc14 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 13);
        double[][] Hpc15 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 14);
        double[][] Hpc16 = calculateHpc(dlaFifthTable, dlaSixthTable, detJ, 15);

        //suma dla HPC
        double[][] sumaHpc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sumaHpc[i][j] = Hpc1[i][j] * wDla4Punktowego[0] * wDla4Punktowego[0]
                        + Hpc2[i][j] * wDla4Punktowego[0] * wDla4Punktowego[1]
                        + Hpc3[i][j] * wDla4Punktowego[0] * wDla4Punktowego[2]
                        + Hpc4[i][j] * wDla4Punktowego[0] * wDla4Punktowego[3]
                        + Hpc5[i][j] * wDla4Punktowego[1] * wDla4Punktowego[0]
                        + Hpc6[i][j] * wDla4Punktowego[1] * wDla4Punktowego[1]
                        + Hpc7[i][j] * wDla4Punktowego[1] * wDla4Punktowego[2]
                        + Hpc8[i][j] * wDla4Punktowego[1] * wDla4Punktowego[3]
                        + Hpc9[i][j] * wDla4Punktowego[2] * wDla4Punktowego[0]
                        + Hpc10[i][j] * wDla4Punktowego[2] * wDla4Punktowego[1]
                        + Hpc11[i][j] * wDla4Punktowego[2] * wDla4Punktowego[2]
                        + Hpc12[i][j] * wDla4Punktowego[2] * wDla4Punktowego[3]
                        + Hpc13[i][j] * wDla4Punktowego[3] * wDla4Punktowego[0]
                        + Hpc14[i][j] * wDla4Punktowego[3] * wDla4Punktowego[1]
                        + Hpc15[i][j] * wDla4Punktowego[3] * wDla4Punktowego[2]
                        + Hpc16[i][j] * wDla4Punktowego[3] * wDla4Punktowego[3];
            }
        }
        System.out.println("Suma Hpc: ");
        showHpc(sumaHpc);
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
        double[][] table2DlaPc1 = new double[firstTable.length][4];
        for (int i = 0; i < firstTable.length; i++) {
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
        double[][] table1DlaPc1 = new double[firstTable.length][4];
        for (int i = 0; i < firstTable.length; i++) {
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

    private static double[][] calculateAndShowSixthArray() {
        //Czwarta tabela od gory
        double[] ksi = new double[]{
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136,
                -0.861136, -0.339981, 0.339981, 0.861136
        };
        double[][] table4 = new double[16][4];
        List<Function<Double, Double>> functions = new ArrayList<>() {
            {
                add(eta -> 0.25 * (eta - 1));
                add(eta -> 0.25 * (-eta - 1));
                add(eta -> 0.25 * (eta + 1));
                add(eta -> 0.25 * (1 - eta));
            }
        };
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                table4[i][j] = functions.get(j).apply(ksi[i]); //[i] w dol, [j] w prawo
            }
        }
        System.out.println("SixthArray");
        showArray(ksi, table4, 16, 4);
        return table4;
    }
    private static double[][] calculateAndShowFifthArray() {
    //piąta tabela od góry
    double[] eta = new double[]{
            -0.861136, -0.861136, -0.861136, -0.861136,
            -0.339981, -0.339981, -0.339981, -0.339981,
            0.339981, 0.339981, 0.339981, 0.339981,
            0.861136, 0.861136, 0.861136, 0.861136
    };
    double[][] table4 = new double[16][4];
    List<Function<Double, Double>> functions = new ArrayList<>() {
        {
            add(ksi -> 0.25 * (ksi - 1));
            add(ksi -> 0.25 * (1 - ksi));
            add(ksi -> 0.25 * (ksi + 1));
            add(ksi -> 0.25 * (-ksi - 1));
        }
    };
    for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 4; j++) {
            table4[i][j] = functions.get(j).apply(eta[i]); //[i] w dol, [j] w prawo
        }
    }
    System.out.println("FifthArray");
    showArray(eta, table4, 16, 4);
    return table4;
}
    private static double[][] calculateAndShowFourthArray() {
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
        return table4;
    }

    private static double[][] calculateAndShowThirdArray() {
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
        return table4;
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
