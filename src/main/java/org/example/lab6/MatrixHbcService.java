package org.example.lab6;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;

import java.util.List;

import static java.lang.Math.sqrt;
import static org.example.lab1.GlobalData.geometricModelsN;

public class MatrixHbcService {
    public static double[][] globalAggregationHBC;
    public static double[] globalAggregationVectorP;


    public static void calculateMatrixHbc_andVectorP(Grid grid, int numberOfPoints) {
        if (numberOfPoints < 2 || numberOfPoints > 4) {
            throw new IllegalArgumentException(
                    "There is no method to calculate matrix Hbc and Vector P with number for nodes = " + numberOfPoints);
        }
        globalAggregationHBC = new double[grid.getNodesNumber()][grid.getNodesNumber()];
        globalAggregationVectorP = new double[grid.getNodesNumber()];
        int length = numberOfPoints * numberOfPoints;
        double[][] ksiEta = getCoordinatesKsiEta(numberOfPoints);

        double[][] geometricModelsValues1 = new double[numberOfPoints][4]; // for wall 1
        double[][] geometricModelsValues2 = new double[numberOfPoints][4]; // for wall 2
        double[][] geometricModelsValues3 = new double[numberOfPoints][4]; // N3
        double[][] geometricModelsValues4 = new double[numberOfPoints][4]; // N4
        if (numberOfPoints == 2) {
            for (int i = 0; i < 4; i++) {
                geometricModelsValues1[0][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
                geometricModelsValues1[1][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
                geometricModelsValues2[0][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);
                geometricModelsValues2[1][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);

                geometricModelsValues3[0][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
                geometricModelsValues3[1][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);
                geometricModelsValues4[0][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
                geometricModelsValues4[1][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
            }
        }
        if (numberOfPoints == 3) {
            for (int i = 0; i < 4; i++) {
                geometricModelsValues1[0][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
                geometricModelsValues1[1][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
                geometricModelsValues1[2][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);
                geometricModelsValues2[0][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);
                geometricModelsValues2[1][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
                geometricModelsValues2[2][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);

                geometricModelsValues3[0][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
                geometricModelsValues3[1][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);
                geometricModelsValues3[2][i] = geometricModelsN(i, ksiEta[8][0], ksiEta[8][1]);
                geometricModelsValues4[0][i] = geometricModelsN(i, ksiEta[9][0], ksiEta[9][1]);
                geometricModelsValues4[1][i] = geometricModelsN(i, ksiEta[10][0], ksiEta[10][1]);
                geometricModelsValues4[2][i] = geometricModelsN(i, ksiEta[11][0], ksiEta[11][1]);
            }
        }
        if (numberOfPoints == 4) {
            for (int i = 0; i < 4; i++) {
                geometricModelsValues1[0][i] = geometricModelsN(i, ksiEta[0][0], ksiEta[0][1]);
                geometricModelsValues1[1][i] = geometricModelsN(i, ksiEta[1][0], ksiEta[1][1]);
                geometricModelsValues1[2][i] = geometricModelsN(i, ksiEta[2][0], ksiEta[2][1]);
                geometricModelsValues1[3][i] = geometricModelsN(i, ksiEta[3][0], ksiEta[3][1]);

                geometricModelsValues2[0][i] = geometricModelsN(i, ksiEta[4][0], ksiEta[4][1]);
                geometricModelsValues2[1][i] = geometricModelsN(i, ksiEta[5][0], ksiEta[5][1]);
                geometricModelsValues2[2][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]);
                geometricModelsValues2[3][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]);

                geometricModelsValues3[0][i] = geometricModelsN(i, ksiEta[8][0], ksiEta[8][1]);
                geometricModelsValues3[1][i] = geometricModelsN(i, ksiEta[9][0], ksiEta[9][1]);
                geometricModelsValues3[2][i] = geometricModelsN(i, ksiEta[10][0], ksiEta[10][1]);
                geometricModelsValues3[3][i] = geometricModelsN(i, ksiEta[11][0], ksiEta[11][1]);

                geometricModelsValues4[0][i] = geometricModelsN(i, ksiEta[12][0], ksiEta[12][1]);
                geometricModelsValues4[1][i] = geometricModelsN(i, ksiEta[13][0], ksiEta[13][1]);
                geometricModelsValues4[2][i] = geometricModelsN(i, ksiEta[14][0], ksiEta[14][1]);
                geometricModelsValues4[3][i] = geometricModelsN(i, ksiEta[15][0], ksiEta[15][1]);
            }
        }

        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();

        double alfaFactor = GlobalData.alfa; // here alfa factor has to be read from file
        double tot = GlobalData.tot; // 1200 from file
        double[][][] BCwall1 = new double[9][4][4];
        double[][][] BCwall2 = new double[9][4][4];
        double[][][] BCwall3 = new double[9][4][4];
        double[][][] BCwall4 = new double[9][4][4];

        double[] weights = GlobalData.getWeightsArray(numberOfPoints);
        for (int e = 0; e < 9; e++) {
            for (int n = 0; n < numberOfPoints; n++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) { // Integral -> alfa*({N}*{N}^T)dS
                        BCwall1[e][i][j] += weights[n] * geometricModelsValues1[n][i] * geometricModelsValues1[n][j];
                        BCwall2[e][i][j] += weights[n] * geometricModelsValues2[n][i] * geometricModelsValues2[n][j];
                        BCwall3[e][i][j] += weights[n] * geometricModelsValues3[n][i] * geometricModelsValues3[n][j];
                        BCwall4[e][i][j] += weights[n] * geometricModelsValues4[n][i] * geometricModelsValues4[n][j];
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
                    if (numberOfPoints == 2) {
                        localP1[i][j] = (weights[0] * (geometricModelsValues1[0][j] * tot) + weights[1] * (geometricModelsValues1[1][j] * tot));
                        localP2[i][j] = (weights[0] * (geometricModelsValues2[0][j] * tot) + weights[1] * (geometricModelsValues2[1][j] * tot));
                        localP3[i][j] = (weights[0] * (geometricModelsValues3[0][j] * tot) + weights[1] * (geometricModelsValues3[1][j] * tot));
                        localP4[i][j] = (weights[0] * (geometricModelsValues4[0][j] * tot) + weights[1] * (geometricModelsValues4[1][j] * tot));
                    }
                    if (numberOfPoints == 3) {
                        localP1[i][j] = (weights[0] * (geometricModelsValues1[0][j] * tot) + weights[1] * (geometricModelsValues1[1][j] * tot) + weights[2] * (geometricModelsValues1[2][j] * tot));
                        localP2[i][j] = (weights[0] * (geometricModelsValues2[0][j] * tot) + weights[1] * (geometricModelsValues2[1][j] * tot) + weights[2] * (geometricModelsValues2[2][j] * tot));
                        localP3[i][j] = (weights[0] * (geometricModelsValues3[0][j] * tot) + weights[1] * (geometricModelsValues3[1][j] * tot) + weights[2] * (geometricModelsValues3[2][j] * tot));
                        localP4[i][j] = (weights[0] * (geometricModelsValues4[0][j] * tot) + weights[1] * (geometricModelsValues4[1][j] * tot) + weights[2] * (geometricModelsValues4[2][j] * tot));
                    }
                    if (numberOfPoints == 4) {
                        localP1[i][j] = (weights[0] * (geometricModelsValues1[0][j] * tot)
                                + weights[1] * (geometricModelsValues1[1][j] * tot)
                                + weights[2] * (geometricModelsValues1[2][j] * tot)
                                + weights[3] * (geometricModelsValues1[3][j] * tot));
                        localP2[i][j] = (weights[0] * (geometricModelsValues2[0][j] * tot)
                                + weights[1] * (geometricModelsValues2[1][j] * tot)
                                + weights[2] * (geometricModelsValues2[2][j] * tot)
                                + weights[3] * (geometricModelsValues2[3][j] * tot));
                        localP3[i][j] = (weights[0] * (geometricModelsValues3[0][j] * tot)
                                + weights[1] * (geometricModelsValues3[1][j] * tot)
                                + weights[2] * (geometricModelsValues3[2][j] * tot)
                                + weights[3] * (geometricModelsValues3[3][j] * tot));
                        localP4[i][j] = (weights[0] * (geometricModelsValues4[0][j] * tot)
                                + weights[1] * (geometricModelsValues4[1][j] * tot)
                                + weights[2] * (geometricModelsValues4[2][j] * tot)
                                + weights[3] * (geometricModelsValues4[3][j] * tot));
                    }
                }
            }
        }
        // ************** Main loop for 9 elements **************
        int elementMax = 9;
        for (int element = 0; element < elementMax; element++) {
            Element element1 = elements.get(element);
            double[] detJWallElement1 = calculateDetJForElement(nodes, element1);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    BCwall1[element][i][j] *= detJWallElement1[0] * alfaFactor;
                    BCwall2[element][i][j] *= detJWallElement1[1] * alfaFactor;
                    BCwall3[element][i][j] *= detJWallElement1[2] * alfaFactor;
                    BCwall4[element][i][j] *= detJWallElement1[3] * alfaFactor;
                }
            }

            for (int j = 0; j < 4; j++) {
                localP1[element][j] *= detJWallElement1[0] * alfaFactor;
                localP2[element][j] *= detJWallElement1[1] * alfaFactor;
                localP3[element][j] *= detJWallElement1[2] * alfaFactor;
                localP4[element][j] *= detJWallElement1[3] * alfaFactor;
            }

            List<Integer> e1IDs = elements.get(elementMax - 1 - element).getIDs();
            double[][] localHbcForElement = calculateAndGetLocalHbc(
                    BCwall1[element], BCwall2[element], BCwall3[element], BCwall4[element], nodes, e1IDs);
            double[] localVectorP = calculateAndGetLocalVectorP(
                    localP1[element], localP2[element], localP3[element], localP4[element], nodes, e1IDs);
            e1IDs = element1.getIDs();
            // ********************* here is aggregation *********************
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    globalAggregationHBC[e1IDs.get(i) - 1][e1IDs.get(j) - 1] += localHbcForElement[i][j];
                }
                globalAggregationVectorP[e1IDs.get(i) - 1] += localVectorP[i];
            }
        }
    }

    private static double[][] getCoordinatesKsiEta(int numberOfPoints) {
        if (numberOfPoints == 2) {
            return new double[][]{
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
            return new double[][]{
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
        if (numberOfPoints == 4) {
            return new double[][]{
                    {-0.861136, -1},
                    {-0.339981, -1},
                    {0.339981, -1},
                    {0.861136, -1},

                    {1, -0.861136},
                    {1, -0.339981},
                    {1, 0.339981},
                    {1, 0.861136},

                    {-0.861136, 1},
                    {-0.339981, 1},
                    {0.339981, 1},
                    {0.861136, 1},

                    {-1, -0.861136},
                    {-1, -0.339981},
                    {-1, 0.339981},
                    {-1, 0.861136}};
        } else {
            throw new IllegalArgumentException("numberOfPoints must be 2 or 3");
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
