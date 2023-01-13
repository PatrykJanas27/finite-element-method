package org.example.lab6;

import org.example.lab1.Element;
import org.example.lab1.GlobalData;
import org.example.lab1.Grid;
import org.example.lab1.Node;
import org.example.lab4.MatrixService;

import java.util.List;

import static org.example.lab6.BorderConditionService.calculateDetJForElement;
import static org.example.lab6.BorderConditionService.geometricModelsN;

@Deprecated
public class VectorP {
    private static double[] globalAggregationVectorP = new double[16];

    public static double[] calculateVectorP(Grid grid) {
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

        double[][] beforeHbc1 = new double[2][4]; // wartosci funkcji ksztaltu
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
            beforeHbc4[0][i] = geometricModelsN(i, ksiEta[7][0], ksiEta[7][1]); // dla Pc42 str 6/17
            beforeHbc4[1][i] = geometricModelsN(i, ksiEta[6][0], ksiEta[6][1]); // dla Pc41 str 6/17 Generacja macierzy H oraz wektora P
        }
//        System.out.println("Before HBC");
//        MatrixService.showTable2Dshort(beforeHbc1);
//        MatrixService.showTable2Dshort(beforeHbc2);
//        MatrixService.showTable2Dshort(beforeHbc3);
//        MatrixService.showTable2Dshort(beforeHbc4);

        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();


//        double detJ = 0.0166667;
        double alfaFactor = GlobalData.alfa; // here alfa factor has to be read from file
        double tot = GlobalData.tot; // 1200 from file
        double[][] localP1 = new double[9][4];
        double[][] localP2 = new double[9][4];
        double[][] localP3 = new double[9][4];
        double[][] localP4 = new double[9][4];
        double[] weight = new double[]{1.0, 1.0};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                localP1[i][j] = alfaFactor * (weight[0] * (beforeHbc1[0][j] * tot) + weight[1] * (beforeHbc1[1][j] * tot));
                localP2[i][j] = alfaFactor * (weight[0] * (beforeHbc2[0][j] * tot) + weight[1] * (beforeHbc2[1][j] * tot));
                localP3[i][j] = alfaFactor * (weight[0] * (beforeHbc3[0][j] * tot) + weight[1] * (beforeHbc3[1][j] * tot));
                localP4[i][j] = alfaFactor * (weight[0] * (beforeHbc4[0][j] * tot) + weight[1] * (beforeHbc4[1][j] * tot));
            }
        }


        int elementMax = 9;
        for (int element = 0; element < elementMax; element++) { // loop for 9 elements
            Element element1 = elements.get(element);
            double[] detJWallElement1 = calculateDetJForElement(nodes, element1); // TODO there is 4 detJ for one element, should it be?
//            double detJWallElement1= 0.0125; // FIXME should be calculated!
            for (int j = 0; j < 4; j++) {
                localP1[element][j] *= detJWallElement1[j];
                localP2[element][j] *= detJWallElement1[j];
                localP3[element][j] *= detJWallElement1[j];
                localP4[element][j] *= detJWallElement1[j];
            }
            List<Integer> e1IDs = elements.get(elementMax - 1 - element).getIDs(); // FIXME here is a big change!!!!!!!
            // FIXME element9 licz dla niego H lokalne + HBC lokalne, a weight agregacji dla tego elementu 9 bierz ID węzłów tak jak dla elementu 1
//            List<Integer> e1IDs = element1.getIDs();
//
            double[] localVectorP = calculateAndGetLocalVectorP(
                    localP1[element], localP2[element], localP3[element], localP4[element], nodes, e1IDs);
            System.out.println("localVectorP for element " + (element + 1) + ": ");
            MatrixService.showTable1D(localVectorP);
            e1IDs = element1.getIDs();
//            // here is aggregation
            for (int i = 0; i < 4; i++) {
                globalAggregationVectorP[e1IDs.get(i) - 1] += localVectorP[i]; // without + !!! just =
            }
        }


        return globalAggregationVectorP;

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
