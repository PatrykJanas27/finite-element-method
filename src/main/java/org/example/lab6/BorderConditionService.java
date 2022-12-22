package org.example.lab6;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab4.MatrixService;
import org.example.lab5.Aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BorderConditionService {
    private double[][] globalH = new double[16][16];

    public static void calculateMatrixHbc(Grid grid, double[][] matrixHForTwoPointIntegration1, GlobalData globalData) {
        double[][] ksiEta = new double[][]{
                {-(1 / Math.sqrt(3)), -1},
                {(1 / Math.sqrt(3)), -1},

                {1, -(1 / Math.sqrt(3))},
                {1, (1 / Math.sqrt(3))},

                {-(1 / Math.sqrt(3)), 1},
                {(1 / Math.sqrt(3)), 1},

                {-1, -(1 / Math.sqrt(3))},
                {-1, (1 / Math.sqrt(3))},
        };

        double[][] beforeHbc1 = new double[2][4];
        double[][] beforeHbc2 = new double[2][4];
        double[][] beforeHbc3 = new double[2][4];
        double[][] beforeHbc4 = new double[2][4];
        for (int i = 0; i < 4; i++) {
            beforeHbc1[0][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
            beforeHbc1[1][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
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
        // detJ for wall1

        Element element1 = elements.get(0);
        double detJWall[] = calculateDetJForElement(nodes, element1);

//        double detJ = 0.0166667;
        double alfaFactor = globalData.getAlfa();
        double[][] BCwall1 = new double[4][4]; //pow1
        double[][] BCwall2 = new double[4][4]; //pow2 from pdf
        double[][] BCwall3 = new double[4][4]; //pow3
        double[][] BCwall4 = new double[4][4]; //pow4
        double[] w = new double[]{1.0, 1.0};

        for (int n = 0; n < 2; n++) { //here is a loop for pc1 and pc2
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    BCwall1[i][j] += w[n] * alfaFactor * beforeHbc1[n][i] * w[n] * beforeHbc1[n][j];
                    BCwall2[i][j] += w[n] * alfaFactor * beforeHbc2[n][i] * w[n] * beforeHbc2[n][j];
                    BCwall3[i][j] += w[n] * alfaFactor * beforeHbc3[n][i] * w[n] * beforeHbc3[n][j];
                    BCwall4[i][j] += w[n] * alfaFactor * beforeHbc4[n][i] * w[n] * beforeHbc4[n][j];
                }
            }
        }
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                BCwall1[i][j] *= detJ;
//                BCwall2[i][j] *= detJ;
//                BCwall3[i][j] *= detJ;
//                BCwall4[i][j] *= detJ;
//            }
//        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                BCwall1[i][j] *= detJWall[0];
                BCwall2[i][j] *= detJWall[1];
                BCwall3[i][j] *= detJWall[2];
                BCwall4[i][j] *= detJWall[3];
            }
        }
        System.out.println("BCwall1: ");
        MatrixService.showTable2D(BCwall1);
        System.out.println("BCwall2: ");
        MatrixService.showTable2D(BCwall2);
        System.out.println("BCwall3: ");
        MatrixService.showTable2D(BCwall3);
        System.out.println("BCwall4: ");
        MatrixService.showTable2D(BCwall4);

        //calculating border conditions for elements



        //**** check which node has border condition (BC) for first element***

        for (int i = 0; i < 9; i++) {
            System.out.println("element " + (i + 1) + ": ");
            for (int j = 0; j < 4; j++) {
                System.out.println(nodes.get((elements.get(i).getIDs().get(j)) - 1).isBC());
            }
        }
        //***** Element 1 and his IDs
        List<Integer> e1IDs = elements.get(0).getIDs();
        System.out.println("element 1: " + e1IDs); // 1, 2, 6, 5
        double[][] aggregationForElements = new double[16][16];
        //******

        //***** Here is aggregation for ->first element
        double[][] aggregationForElement1 = calcualteHbcAndGetAggregationForElement(
                grid, matrixHForTwoPointIntegration1, BCwall1, BCwall2, BCwall3, BCwall4, nodes, e1IDs);

        //**** Here is aggregation for *ALL* elements
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                aggregationForElements[i][j] += aggregationForElement1[i][j];
            }
        }
        //***************************


        System.out.println("aggregationForElements: ");
        MatrixService.showTable2Dshort(aggregationForElements);
    }

    private static double[] calculateDetJForElement(List<Node> nodes, Element element1) {
        double[] detJWall = new double[4];
        List<Integer> element1IDs = element1.getIDs();
        Integer id1 = element1IDs.get(0);
        Integer id2 = element1IDs.get(1);
        Integer id3 = element1IDs.get(2);
        Integer id4 = element1IDs.get(3);
        Node node1 = nodes.get(id1);
        Node node2 = nodes.get(id2);
        Node node3 = nodes.get(id3);
        Node node4 = nodes.get(id4);
        double x1 = node1.getX();
        double y1 = node1.getY();
        double x2 = node2.getX();
        double y2 = node2.getY();
        double x3 = node3.getX();
        double y3 = node3.getY();
        double y4 = node4.getY();
        double x4 = node4.getX();
        //for horizontal walls __
        if(y1 == y2){
            detJWall[0] = (Math.abs(x1 - x2))/2.0;
            System.out.println("detJWall1" + detJWall[0]);
        }
        if(y1 != y2) {
            detJWall[0] = (Math.sqrt(Math.pow(Math.abs(y2 - y1),2) + Math.pow(Math.abs(x1 - x2),2)))/2.0;
        }
        if(y3 == y4){
            detJWall[2] = (Math.abs(x3 - x4))/2.0;
        }
        if(y3 != y4) {
            detJWall[2] = (Math.sqrt(Math.pow(Math.abs(y4 - y3),2) + Math.pow(Math.abs(x3 - x4),2)))/2.0;
        }
        //for vertical walls |
        if(x2 == x3){
            detJWall[1] = (Math.abs(y2 - y3))/2.0;
        }
        if(x2 != x3) {
            detJWall[1] = (Math.sqrt(Math.pow(Math.abs(x3 - x2),2) + Math.pow(Math.abs(y2 - y3),2)))/2.0;
        }
        if(x1 == x4){
            detJWall[3] = (Math.abs(y1 - y4))/2.0;
        }
        if(x1 != x4) {
            detJWall[3] = (Math.sqrt(Math.pow(Math.abs(x4 - x1),2) + Math.pow(Math.abs(y1 - y4),2)))/2.0;
        }
        //***
        return detJWall;
    }


    private static double[][] calcualteHbcAndGetAggregationForElement(Grid grid, double[][] matrixHForTwoPointIntegration1, double[][] BCwall1, double[][] BCwall2, double[][] BCwall3, double[][] BCwall4, List<Node> nodes, List<Integer> e1IDs) {
        //Conditions, check if node has border condition ***********************
        double[][] localBC1 = new double[4][4];
        if (nodes.get((e1IDs.get(0)) - 1).isBC() && nodes.get((e1IDs.get(1)) - 1).isBC()) { //top right && top left ^^
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC1[i][j] = BCwall3[i][j]; //FIXME here how to do that?
                }
            }
        }

        double[][] localBC2 = new double[4][4];
        if (nodes.get((e1IDs.get(1)) - 1).isBC() && nodes.get((e1IDs.get(2)) - 1).isBC()) { //top left && bottom left |<- |
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC2[i][j] = BCwall4[i][j];
                }
            }
        }

        double[][] localBC3 = new double[4][4];
        if (nodes.get((e1IDs.get(2)) - 1).isBC() && nodes.get((e1IDs.get(3)) - 1).isBC()) { //bottom left && bottom right __
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC3[i][j] = BCwall1[i][j];
                }
            }
        }

        double[][] localBC4 = new double[4][4];
        if (nodes.get((e1IDs.get(3)) - 1).isBC() && nodes.get((e1IDs.get(0)) - 1).isBC()) { //bottom right && top right | ->|
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    localBC4[i][j] = BCwall2[i][j];
                }
            }
        }
        System.out.println("Hbc wall local1: ");
        MatrixService.showTable2Dshort(localBC1);
        System.out.println("Hbc local2: ");
        MatrixService.showTable2Dshort(localBC2);
        System.out.println("Hbc local3: ");
        MatrixService.showTable2Dshort(localBC3);
        System.out.println("Hbc local4: ");
        MatrixService.showTable2Dshort(localBC4);
        //************************ now we have 4 matrix, every for one side of wall

        //*******
        double[][] localHbc = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                localHbc[i][j] += localBC1[i][j] + localBC2[i][j] + localBC3[i][j] + localBC4[i][j];
            }
        }
        System.out.println("Hbc local: ");
        MatrixService.showTable2Dshort(localHbc);
        //*******

        //******* Summing localHBC + localH **
        double[][] localHBC_plus_localH = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                localHBC_plus_localH[i][j] += localBC1[i][j] + localBC2[i][j] + localBC3[i][j] + localBC4[i][j] + matrixHForTwoPointIntegration1[i][j];
            }
        }
        System.out.println("Hbc local + H: ");
        MatrixService.showTable2Dshort(localHBC_plus_localH);
        //*******

        double[][] aggregationForElement1 = Aggregation.calculateAggregation(grid, localHBC_plus_localH);
        return aggregationForElement1;
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
