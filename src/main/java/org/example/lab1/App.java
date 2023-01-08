package org.example.lab1;

import org.example.data.InputHandler;
import org.example.lab4.MatrixHService;
import org.example.lab4.MatrixService;
import org.example.lab6.BorderConditionService;
import org.example.lab6.GaussianEliminationService;
import org.example.lab6.VectorP;
import org.example.lab7.MatrixC;

import java.io.FileNotFoundException;
import java.util.List;

import static org.example.lab5.Aggregation.calculateAggregation;

public class App {

    private static final String FILE_NAME1 = "src/main/resources/lab1/Test1_4_4.txt";
    private static final String FILE_NAME2 = "src/main/resources/lab1/Test2_4_4_MixGrid.txt";
    private static final String FILE_NAME3 = "src/main/resources/lab1/Test3_31_31_kwadrat.txt";

    public static void main(String[] args) throws FileNotFoundException, NumberFormatException {

        Grid grid = new Grid();
        GlobalData globalData = new GlobalData();
        InputHandler.readFile(FILE_NAME1, grid, globalData);
        System.out.println(globalData);
        System.out.println(grid);
        List<Element> elements = grid.getElements();

        List<Node> nodes = grid.getNodes();

        System.out.println(elements.get(0));
        System.out.println(elements.get(0).getIDs());
        System.out.println(nodes.get(elements.get(0).getIDs().get(0)));
        System.out.println(nodes.get(elements.get(0).getIDs().get(1)));
        System.out.println(nodes.get(elements.get(0).getIDs().get(2)));
        System.out.println(nodes.get(elements.get(0).getIDs().get(3)));
        System.out.println("size: " + elements.size());
        System.out.println("element 9: " + elements.get(8));
        double[][] HG4 = new double[9][9];
        double[] pcx = new double[4];
        double[] pcy = new double[4];
//        for (int e = 0; e < elements.size(); e++) {
//            System.out.println("element: " + (e+1));
//            Element element = elements.get(e);
//            System.out.println("element id: " + element);
        Element element = elements.get(0);
        for (int i = 0; i < nodes.size(); i++) { // < 4
            for (int j = 0; j < 4; j++) { // 4 wspolrzedne x i y
                pcx[j] = nodes.get(element.getIDs().get(j) - 1).getX();
                pcy[j] = nodes.get(element.getIDs().get(j) - 1).getY();
            }
        }
//        }

        //****************** Here for matrix H
        double[][] mainMatrix = MatrixService.getMainMatrix(pcx, pcy);
        double[][] matrixHForTwoPointIntegration1 = MatrixHService.getMatrixHForTwoPointIntegrationWithGlobalData(mainMatrix, globalData);
        System.out.println("matrixHForTwoPointIntegration1: ");
        MatrixService.showTable2D(matrixHForTwoPointIntegration1); //TODO MatrixH is always the same for every element?


        //************************ Here for HBC
        double[][] globalAggregationHBC = BorderConditionService.calculateMatrixHbc(grid, globalData);
        System.out.println("globalAggregationHBC: ");
        MatrixService.showTable2Dshort(globalAggregationHBC);

        double[][] globalAggregationH = calculateAggregation(grid, matrixHForTwoPointIntegration1);
        System.out.println("globalAggregationH: ");
        MatrixService.showTable2Dshort(globalAggregationH);

        double[][] globalAggregationHplusHBC = new double[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationHplusHBC[i][j] = globalAggregationHBC[i][j] + globalAggregationH[i][j];
            }
        }
        System.out.println("globalAggregationHplusHBC: ");
        MatrixService.showTable2Dshort(globalAggregationHplusHBC);


        double[] globalAggregationVectorP = VectorP.calculateVectorP(grid, globalData);
        System.out.println("globalAggregationVectorP: ");
        MatrixService.showTable1D(globalAggregationVectorP);

        // [H]{t}+{P}=0
        double[] solutionForSystemOfEquations = GaussianEliminationService.findSolutionForSystemOfEquations(
                globalAggregationHplusHBC, globalData.getTot(), globalAggregationVectorP);
        System.out.println("solutionForSystemOfEquations");
        MatrixService.showTable1D(solutionForSystemOfEquations);

        // Matrix C // <----- to this point everything is good
        double[][] globalAggregationMatrixC = MatrixC.calculateMatrixC(grid, globalData);
        System.out.println("globalAggregationMatrixC: ");
        MatrixService.showTable2Dshort(globalAggregationMatrixC);

        // [C]/dT
        int simulationStepTime = globalData.getSimulationStepTime();
        double[][] globalAggregationMatrixCByDeltaTau = new double[16][16];
        for (int i = 0; i < globalAggregationMatrixC.length; i++) {
            for (int j = 0; j < globalAggregationMatrixC[i].length; j++) {
                globalAggregationMatrixCByDeltaTau[i][j] = globalAggregationMatrixC[i][j] / simulationStepTime;
                // delta tay -> simpulation step time
            }
        }
        System.out.println("globalAggregationMatrixCByDeltaTau ->> [C]/dT: ");
        MatrixService.showTable2Dshort(globalAggregationMatrixCByDeltaTau);

        // Martix [H] = [H]+[C]/dT
        double[][] globalAggregationMatrixHPlusMatrixCByDeltaTau = new double[16][16];
        for (int i = 0; i < globalAggregationMatrixC.length; i++) {
            for (int j = 0; j < globalAggregationMatrixC[i].length; j++) {
                globalAggregationMatrixHPlusMatrixCByDeltaTau[i][j] = globalAggregationHplusHBC[i][j]
                        + globalAggregationMatrixCByDeltaTau[i][j];
            }
        }
        System.out.println("globalAggregationMatrixHPlusMatrixCByDeltaTau ->> Martix [H] = [H]+[C]/dT : ");
        MatrixService.showTable2Dshort(globalAggregationMatrixHPlusMatrixCByDeltaTau);
        double deltaT;
        deltaT = globalData.getSimulationStepTime();
        int iteration = 0;
        int actualTime = 0;
        double minimum;
        double maximum;
        double[] t1 = new double[16];


        // {[C]/dT}*{T0}
        int initialTemp = globalData.getInitialTemp();
        double[][] globalAggregationMatrixCByDeltaTauMultiplyT0 = new double[16][16];
        double[] CpodT = new double[16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationMatrixCByDeltaTauMultiplyT0[i][j] += globalAggregationMatrixCByDeltaTau[i][j] * initialTemp; //CG4koniec <<-----
                CpodT[i] += globalAggregationMatrixCByDeltaTauMultiplyT0[i][j];
            }
        }

//            // FIXME doesnt work
        // {P} = {P}+{[C]/dT}*{T0}
        double[] vectorP_plus_matrixCByDeltaTauMultipliedByT0 = new double[16];
        for (int i = 0; i < 16; i++) {
            vectorP_plus_matrixCByDeltaTauMultipliedByT0[i] += globalAggregationVectorP[i] + CpodT[i];
        }
        System.out.println("vectorP_plus_matrixCByDeltaTauMultipliedByT0 ->> Vector ([{P}+{[C]/dT}*{T0}): ");
        MatrixService.showTable1D(vectorP_plus_matrixCByDeltaTauMultipliedByT0);
    }


}
