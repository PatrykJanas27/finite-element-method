package org.example.lab6;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab4.MatrixService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BorderConditionService {
    private static double[][] globalAggregationHBC = new double[16][16];


    public static double[][] calculateMatrixHbc(Grid grid, GlobalData globalData) {
        double[][] ksiEta = new double[][]{
                {-(1 / Math.sqrt(3)), -1},   // pc11 str 13/17 "generacja macierzy H oraz wektora P"
                {(1 / Math.sqrt(3)), -1},   // pc12

                {1, -(1 / Math.sqrt(3))},   // pc21
                {1, (1 / Math.sqrt(3))},    // pc22

                {-(1 / Math.sqrt(3)), 1},   // pc31
                {(1 / Math.sqrt(3)), 1},    // pc32

                {-1, -(1 / Math.sqrt(3))},  // pc41
                {-1, (1 / Math.sqrt(3))},   // pc42
        };
        // Wartości dla funkcji kształtu
        double[][] beforeHbc1 = new double[2][4]; // for wall 1
        double[][] beforeHbc2 = new double[2][4]; // for wall 2
        double[][] beforeHbc3 = new double[2][4]; // N3
        double[][] beforeHbc4 = new double[2][4]; // N4
        for (int i = 0; i < 4; i++) {
            beforeHbc1[0][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]); // (1 / Math.sqrt(3)), -1
            beforeHbc1[1][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]); // -(1 / Math.sqrt(3)), -1
            beforeHbc2[0][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);
            beforeHbc2[1][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);

            beforeHbc3[0][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);
            beforeHbc3[1][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
            beforeHbc4[0][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
            beforeHbc4[1][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
        }
        System.out.println("Before Hbc 4: ");
        MatrixService.showTable2D(beforeHbc4);
        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();


//        double detJ = 0.0166667;
        double alfaFactor = globalData.getAlfa(); // here alfa factor has to be read from file
        double[][][] BCwall1E1 = new double[9][4][4]; //pow1
        double[][][] BCwall2E1 = new double[9][4][4]; //pow1
        double[][][] BCwall3E1 = new double[9][4][4]; //pow1
        double[][][] BCwall4E1 = new double[9][4][4]; //pow1

        double[] weight = new double[]{1.0, 1.0};
        for (int e = 0; e < 9; e++) {
            for (int n = 0; n < 2; n++) { // here is a loop for pc1 and pc2
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) { // Integral -> alfa*({N}*{N}^T)dS
                        BCwall1E1[e][i][j] += weight[n] * alfaFactor * beforeHbc1[n][i] * weight[n] * beforeHbc1[n][j];
                        BCwall2E1[e][i][j] += weight[n] * alfaFactor * beforeHbc2[n][i] * weight[n] * beforeHbc2[n][j];
                        BCwall3E1[e][i][j] += weight[n] * alfaFactor * beforeHbc3[n][i] * weight[n] * beforeHbc3[n][j];
                        BCwall4E1[e][i][j] += weight[n] * alfaFactor * beforeHbc4[n][i] * weight[n] * beforeHbc4[n][j];
                    }
                }
            }
        }
        int elementMax = 9;
        for (int element = 0; element < elementMax; element++) { // loop for 9 elements
            Element element1 = elements.get(element);
            double[] detJWallElement1 = calculateDetJForElement(nodes, element1); // TODO there is 4 detJ for one element, should it be?
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    BCwall1E1[element][i][j] *= detJWallElement1[0];
                    BCwall2E1[element][i][j] *= detJWallElement1[1];
                    BCwall3E1[element][i][j] *= detJWallElement1[2];
                    BCwall4E1[element][i][j] *= detJWallElement1[3];
                }
            }

            //calculating border conditions for elements
            //**** check which node has border condition (BC) for first element***
//            for (int i = 0; i < 9; i++) {
//                System.out.println("element " + (i + 1) + ": ");
//                for (int j = 0; j < 4; j++) {
//                    System.out.println(nodes.get((elements.get(i).getIDs().get(j)) - 1).isBC());
//                }
//            }
            //***** Element 1 and his IDs
//            List<Integer> e1IDs = element1.getIDs();
            List<Integer> e1IDs = elements.get(elementMax - 1 - element).getIDs(); // FIXME here is a big change!!!!!!!
            // FIXME element9 licz dla niego H lokalne + HBC lokalne, a weight agregacji dla tego elementu 9 bierz ID węzłów tak jak dla elementu 1

            System.out.println("element " + (element + 1) + ": " + e1IDs); // 1, 2, 6, 5

            //***** Here is localHBC for -> first element
            double[][] localHbcForElement = calculateAndGetLocalHbc(
                    BCwall1E1[element], BCwall2E1[element], BCwall3E1[element], BCwall4E1[element], nodes, e1IDs);
            System.out.println("localHbcForElement: ");
            MatrixService.showTable2Dshort(localHbcForElement);
            e1IDs = element1.getIDs(); // FIXME here is a big change!!!!!!!!!!!!!!!!!!!!!!!!
            // FIXME  // element9 licz dla niego H lokalne + HBC lokalne, a weight agregacji dla tego elementu 9 bierz ID węzłów tak jak dla elementu 1

            // here is aggregation
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationHBC[e1IDs.get(i) - 1][e1IDs.get(j) - 1] += localHbcForElement[i][j]; // without + !!! just = ??????
                }
            }
        }


        return globalAggregationHBC;
    }

    public static double[] calculateDetJForElement(List<Node> nodes, Element element1) {
        double[] detJWall = new double[4];
        List<Integer> element1IDs = element1.getIDs();
        Integer id1 = element1IDs.get(0);
        Integer id2 = element1IDs.get(1);
        Integer id3 = element1IDs.get(2);
        Integer id4 = element1IDs.get(3);
        Node node1 = nodes.get(id1 - 1); // has to be id minus 1
        Node node2 = nodes.get(id2 - 1);
        Node node3 = nodes.get(id3 - 1);
        Node node4 = nodes.get(id4 - 1);
        double x1 = node1.getX();
        double y1 = node1.getY();
        double x2 = node2.getX();
        double y2 = node2.getY();
        double x3 = node3.getX();
        double y3 = node3.getY();
        double y4 = node4.getY();
        double x4 = node4.getX();
        //for horizontal walls __
        if (y1 == y2) {
            detJWall[0] = (Math.abs(x1 - x2)) / 2.0;
        }
        if (y1 != y2) {
            detJWall[0] = (Math.sqrt(Math.pow(Math.abs(y2 - y1), 2) + Math.pow(Math.abs(x1 - x2), 2))) / 2.0;
        }
        if (y3 == y4) {
            detJWall[2] = (Math.abs(x3 - x4)) / 2.0;
        }
        if (y3 != y4) {
            detJWall[2] = (Math.sqrt(Math.pow(Math.abs(y4 - y3), 2) + Math.pow(Math.abs(x3 - x4), 2))) / 2.0;
        }
        //for vertical walls |
        if (x2 == x3) {
            detJWall[1] = (Math.abs(y2 - y3)) / 2.0;
        }
        if (x2 != x3) {
            detJWall[1] = (Math.sqrt(Math.pow(Math.abs(x3 - x2), 2) + Math.pow(Math.abs(y2 - y3), 2))) / 2.0;
        }
        if (x1 == x4) {
            detJWall[3] = (Math.abs(y1 - y4)) / 2.0;
        }
        if (x1 != x4) {
            detJWall[3] = (Math.sqrt(Math.pow(Math.abs(x4 - x1), 2) + Math.pow(Math.abs(y1 - y4), 2))) / 2.0;
        }
        //***
        return detJWall;
    }


    private static double[][] calculateAndGetLocalHbc(
            double[][] BCwall1, double[][] BCwall2, double[][] BCwall3, double[][] BCwall4,
            List<Node> nodes, List<Integer> eIDs) {
        //Conditions, check if node has border condition ***********************
        double[][] localBC1 = new double[4][4];
        if (nodes.get((eIDs.get(0)) - 1).isBC() && nodes.get((eIDs.get(1)) - 1).isBC()) { //top right && top left ^^
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC1[i][j] = BCwall3[i][j]; //FIXME here how to do that?
                }
            }
        }

        double[][] localBC2 = new double[4][4];
        if (nodes.get((eIDs.get(1)) - 1).isBC() && nodes.get((eIDs.get(2)) - 1).isBC()) { //top left && bottom left |<- |
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC2[i][j] = BCwall4[i][j];
                }
            }
        }

        double[][] localBC3 = new double[4][4];
        if (nodes.get((eIDs.get(2)) - 1).isBC() && nodes.get((eIDs.get(3)) - 1).isBC()) { //bottom left && bottom right __
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC3[i][j] = BCwall1[i][j];
                }
            }
        }

        double[][] localBC4 = new double[4][4];
        if (nodes.get((eIDs.get(3)) - 1).isBC() && nodes.get((eIDs.get(0)) - 1).isBC()) { //bottom right && top right | ->|
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC4[i][j] = BCwall2[i][j];
                }
            }
        }
//        System.out.println("Hbc wall local1: ");
//        MatrixService.showTable2Dshort(localBC1);
//        System.out.println("Hbc wall local2: ");
//        MatrixService.showTable2Dshort(localBC2);
//        System.out.println("Hbc wall local3: ");
//        MatrixService.showTable2Dshort(localBC3);
//        System.out.println("Hbc wall local4: ");
//        MatrixService.showTable2Dshort(localBC4);
        //************************ now we have 4 matrix, every for one side of wall

        //*******
        double[][] localHbc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                localHbc[i][j] += localBC1[i][j] + localBC2[i][j] + localBC3[i][j] + localBC4[i][j];
            }
        }
//        System.out.println("Hbc local: ");
//        MatrixService.showTable2Dshort(localHbc);
        //*******

        return localHbc;
    }

    public static Double geometricModelsN(int whichOneFrom1To4, double ksi, double eta) {
        List<BiFunction<Double, Double, Double>> geometricModels = new ArrayList<>() {
            {
                add((ksi, eta) -> 0.25 * (1 - ksi) * (1 - eta));
                add((ksi, eta) -> 0.25 * (1 + ksi) * (1 - eta));
                add((ksi, eta) -> 0.25 * (1 + ksi) * (1 + eta));
                add((ksi, eta) -> 0.25 * (1 - ksi) * (1 + eta));
            }
        };
        return geometricModels.get(whichOneFrom1To4).apply(ksi, eta);
    }

    public static TriFunction<Double, Double, Double, Double> getBorderConditionTriFunction() {
        TriFunction<Double, Double, Double, Double> heatBorderCondition = new TriFunction<Double, Double, Double, Double>() {
            @Override
            public Double apply(Double alfaFactor, Double temperature, Double temperatureAmb) {
                return alfaFactor * (temperature - temperatureAmb);
            }
        };
        return heatBorderCondition;
    }
}
