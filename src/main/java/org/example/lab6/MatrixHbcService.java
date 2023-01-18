package org.example.lab6;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.Math.sqrt;

public class MatrixHbcService {
    public static double[][] globalAggregationHBC;
    public static double[] globalAggregationVectorP;


    public static void calculateMatrixHbc_andVectorP(Grid grid, int numberOfPoints) {
        globalAggregationHBC = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        globalAggregationVectorP = new double[grid.getNodesNumber()];
        int length = numberOfPoints * numberOfPoints;
        double[][] ksiEta = new double[length][2];
        if (numberOfPoints != 3) {
            ksiEta = new double[][]{
                    {-(1 / Math.sqrt(3)), -1},   // pc11 str 13/17 "generacja macierzy H oraz wektora P"
                    {(1 / Math.sqrt(3)), -1},   // pc12

                    {1, -(1 / Math.sqrt(3))},   // pc21
                    {1, (1 / Math.sqrt(3))},    // pc22

                    {-(1 / Math.sqrt(3)), 1},   // pc31
                    {(1 / Math.sqrt(3)), 1},    // pc32

                    {-1, -(1 / Math.sqrt(3))},  // pc41
                    {-1, (1 / Math.sqrt(3))},   // pc42
            };
        }
        if (numberOfPoints == 3) {
            ksiEta = new double[][]{
                    {-sqrt(3.0 / 5), -1},
                    {0, -1},
                    {sqrt(3.0 / 5), -1},

                    {1, -sqrt(3.0 / 5)},
                    {1, 0},
                    {1, sqrt(3.0 / 5)},

                    {-sqrt(3.0 / 5), 1},
                    {0, 1},
                    {sqrt(3.0 / 5), 1},

                    {-1, -sqrt(3.0 / 5)},
                    {-1, 0},
                    {-1, sqrt(3.0 / 5)},
            };
        }

        // Wartości dla funkcji kształtu
        double[][] beforeHbc1 = new double[numberOfPoints][4]; // for wall 1
        double[][] beforeHbc2 = new double[numberOfPoints][4]; // for wall 2
        double[][] beforeHbc3 = new double[numberOfPoints][4]; // N3
        double[][] beforeHbc4 = new double[numberOfPoints][4]; // N4
        if (numberOfPoints == 2) {
            for (int i = 0; i < 4; i++) {
                beforeHbc1[0][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]); // (1 / Math.sqrt(3)), -1
                beforeHbc1[1][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]); // -(1 / Math.sqrt(3)), -1
                beforeHbc2[0][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);
                beforeHbc2[1][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);

                beforeHbc3[0][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
                beforeHbc3[1][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);
                beforeHbc4[0][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
                beforeHbc4[1][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
            }
        }
        if (numberOfPoints == 3) {
            for (int i = 0; i < 4; i++) {
                beforeHbc1[0][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
                beforeHbc1[1][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
                beforeHbc1[2][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);
                beforeHbc2[0][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);
                beforeHbc2[1][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
                beforeHbc2[2][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);

                beforeHbc3[0][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
                beforeHbc3[1][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
                beforeHbc3[2][i] = geometricModelsN(i, ksiEta[8][0], ksiEta[8][1]);
                beforeHbc4[0][i] = geometricModelsN(i, ksiEta[9][0], ksiEta[9][1]);
                beforeHbc4[1][i] = geometricModelsN(i, ksiEta[10][0], ksiEta[10][1]);
                beforeHbc4[2][i] = geometricModelsN(i, ksiEta[11][0], ksiEta[11][1]);
            }
        }

//        System.out.println("Before Hbc 4: ");
//        MatrixService.showTable2D(beforeHbc4);
        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();


//        double detJ = 0.0166667;
        double alfaFactor = GlobalData.alfa; // here alfa factor has to be read from file
        double tot = GlobalData.tot; // 1200 from file
        double[][][] BCwall1E1 = new double[9][4][4]; //pow1
        double[][][] BCwall2E1 = new double[9][4][4]; //pow1
        double[][][] BCwall3E1 = new double[9][4][4]; //pow1
        double[][][] BCwall4E1 = new double[9][4][4]; //pow1

//        double[] weights = new double[]{1.0, 1.0}; //FIXME for number of pc
        double[] weights = GlobalData.getWeightsArray(numberOfPoints);
        for (int e = 0; e < 9; e++) { //FIXME for number of elements
            for (int n = 0; n < numberOfPoints; n++) { // here is a loop for pc1 and pc2 //FIXME for numberOfElements
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) { // Integral -> alfa*({N}*{N}^T)dS
//                        BCwall1E1[e][i][j] += weights[n] * beforeHbc1[n][i] * weights[n] * beforeHbc1[n][j];
//                        BCwall2E1[e][i][j] += weights[n] * beforeHbc2[n][i] * weights[n] * beforeHbc2[n][j];
//                        BCwall3E1[e][i][j] += weights[n] * beforeHbc3[n][i] * weights[n] * beforeHbc3[n][j];
//                        BCwall4E1[e][i][j] += weights[n] * beforeHbc4[n][i] * weights[n] * beforeHbc4[n][j];
                        //FIXME top or bellow??
                        BCwall1E1[e][i][j] += weights[n] * beforeHbc1[n][i] * beforeHbc1[n][j];
                        BCwall2E1[e][i][j] += weights[n] * beforeHbc2[n][i] * beforeHbc2[n][j];
                        BCwall3E1[e][i][j] += weights[n] * beforeHbc3[n][i] * beforeHbc3[n][j];
                        BCwall4E1[e][i][j] += weights[n] * beforeHbc4[n][i] * beforeHbc4[n][j];
                    }
                }
            }
        }

        double[][] localP1 = new double[9][4];
        double[][] localP2 = new double[9][4];
        double[][] localP3 = new double[9][4];
        double[][] localP4 = new double[9][4];
        for (int i = 0; i < 9; i++) {
            for (int n = 0; n < numberOfPoints; n++) {
                for (int j = 0; j < 4; j++) {
                    if(numberOfPoints==2){
                        localP1[i][j] =   (weights[0] * (beforeHbc1[0][j] * tot) + weights[1] * (beforeHbc1[1][j] * tot));
                        localP2[i][j] =   (weights[0] * (beforeHbc2[0][j] * tot) + weights[1] * (beforeHbc2[1][j] * tot));
                        localP3[i][j] =   (weights[0] * (beforeHbc3[0][j] * tot) + weights[1] * (beforeHbc3[1][j] * tot));
                        localP4[i][j] =   (weights[0] * (beforeHbc4[0][j] * tot) + weights[1] * (beforeHbc4[1][j] * tot));
                    }
                    if(numberOfPoints==3){
                        localP1[i][j] =   (weights[0] * (beforeHbc1[0][j] * tot) + weights[1] * (beforeHbc1[1][j] * tot)+ weights[2] * (beforeHbc1[2][j] * tot));
                        localP2[i][j] =   (weights[0] * (beforeHbc2[0][j] * tot) + weights[1] * (beforeHbc2[1][j] * tot)+ weights[2] * (beforeHbc2[2][j] * tot));
                        localP3[i][j] =   (weights[0] * (beforeHbc3[0][j] * tot) + weights[1] * (beforeHbc3[1][j] * tot)+ weights[2] * (beforeHbc3[2][j] * tot));
                        localP4[i][j] =   (weights[0] * (beforeHbc4[0][j] * tot) + weights[1] * (beforeHbc4[1][j] * tot)+ weights[2] * (beforeHbc4[2][j] * tot));
                    }
                }
            }
        }
        int elementMax = 9;
        for (int element = 0; element < elementMax; element++) { // loop for 9 elements
            Element element1 = elements.get(element);
            double[] detJWallElement1 = calculateDetJForElement(nodes, element1);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    BCwall1E1[element][i][j] *= detJWallElement1[0] * alfaFactor;
                    BCwall2E1[element][i][j] *= detJWallElement1[1] * alfaFactor;
                    BCwall3E1[element][i][j] *= detJWallElement1[2] * alfaFactor;
                    BCwall4E1[element][i][j] *= detJWallElement1[3] * alfaFactor;
                }
            }

            for (int j = 0; j < 4; j++) {
                localP1[element][j] *= detJWallElement1[0] * alfaFactor;
                localP2[element][j] *= detJWallElement1[1] * alfaFactor;
                localP3[element][j] *= detJWallElement1[2] * alfaFactor;
                localP4[element][j] *= detJWallElement1[3] * alfaFactor;
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
            // FIXME element9 licz dla niego H lokalne + HBC lokalne, a weights agregacji dla tego elementu 9 bierz ID węzłów tak jak dla elementu 1

//            System.out.println("element " + (element + 1) + ": " + e1IDs); // 1, 2, 6, 5

            //***** Here is localHBC for -> first element
            double[][] localHbcForElement = calculateAndGetLocalHbc(
                    BCwall1E1[element], BCwall2E1[element], BCwall3E1[element], BCwall4E1[element], nodes, e1IDs);
//            System.out.println("localHbcForElement: ");
//            MatrixService.showTable2Dshort(localHbcForElement);
            double[] localVectorP = calculateAndGetLocalVectorP(
                    localP1[element], localP2[element], localP3[element], localP4[element], nodes, e1IDs);
//            System.out.println("localVectorP for element " + (element + 1) + ": ");
//            MatrixService.showTable1D(localVectorP);
            e1IDs = element1.getIDs(); // FIXME here is a big change!!!!!!!!!!!!!!!!!!!!!!!!
            // FIXME  // element9 licz dla niego H lokalne + HBC lokalne, a weights agregacji dla tego elementu 9 bierz ID węzłów tak jak dla elementu 1

            // here is aggregation
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationHBC[e1IDs.get(i) - 1][e1IDs.get(j) - 1] += localHbcForElement[i][j]; // without + !!! just = ??????
                }
            }
            for (int i = 0; i < 4; i++) {
                globalAggregationVectorP[e1IDs.get(i) - 1] += localVectorP[i]; // without + !!! just =
            }
        }
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
                    localBC1[i][j] = BCwall3[i][j];
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

    private static double[] calculateAndGetLocalVectorP(
            double[] localP1, double[] localP2, double[] localP3, double[] localP4,
            List<Node> nodes, List<Integer> eIDs) {
        //Conditions, check if node has border condition ***********************
        double[] localBC1 = new double[4];
        if (nodes.get((eIDs.get(0)) - 1).isBC() && nodes.get((eIDs.get(1)) - 1).isBC()) { //top right && top left ^^
            for (int i = 0; i < 4; i++) {
                localBC1[i] = localP3[i]; //FIXME here how to do that?
            }
        }

        double[] localBC2 = new double[4];
        if (nodes.get((eIDs.get(1)) - 1).isBC() && nodes.get((eIDs.get(2)) - 1).isBC()) { //top left && bottom left |<- |
            for (int i = 0; i < 4; i++) {
                localBC2[i] = localP4[i];
            }
        }

        double[] localBC3 = new double[4];
        if (nodes.get((eIDs.get(2)) - 1).isBC() && nodes.get((eIDs.get(3)) - 1).isBC()) { //bottom left && bottom right __
            for (int i = 0; i < 4; i++) {
                localBC3[i] = localP1[i];
            }
        }

        double[] localBC4 = new double[4];
        if (nodes.get((eIDs.get(3)) - 1).isBC() && nodes.get((eIDs.get(0)) - 1).isBC()) { //bottom right && top right | ->|
            for (int i = 0; i < 4; i++) {
                localBC4[i] = localP2[i];
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
        double[] localVectorP = new double[4];
        for (int j = 0; j < 4; j++) {
            localVectorP[j] += localBC1[j] + localBC2[j] + localBC3[j] + localBC4[j];
        }
//        System.out.println("Hbc local: ");
//        MatrixService.showTable2Dshort(localHbc);
        //*******

        return localVectorP;
    }
}
