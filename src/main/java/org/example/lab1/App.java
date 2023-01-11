package org.example.lab1;

import org.example.data.InputHandler;
import org.example.lab4.MatrixHService;
import org.example.lab4.MatrixService;

import java.io.FileNotFoundException;
import java.util.List;

public class App {

    private static final String FILE_NAME1 = "src/main/resources/lab1/Test1_4_4.txt";
    private static final String FILE_NAME2 = "src/main/resources/lab1/Test2_4_4_MixGrid.txt";
    private static final String FILE_NAME3 = "src/main/resources/lab1/Test3_31_31_kwadrat.txt";

    public static void main(String[] args) throws FileNotFoundException, NumberFormatException {

        Grid grid = InputHandler.readFile(FILE_NAME1);
        System.out.println(grid);
        List<Element> elements = grid.getElements();
        List<Node> nodes = grid.getNodes();

        ///===============
//        double[] pcx = new double[4];
//        double[] pcy = new double[4];
//        Element element = elements.get(0);
//        for (int i = 0; i < nodes.size(); i++) { // < 4
//            for (int j = 0; j < 4; j++) { // 4 wspolrzedne x i y
//                pcx[j] = nodes.get(element.getIDs().get(j) - 1).getX();
//                pcy[j] = nodes.get(element.getIDs().get(j) - 1).getY();
//            }
//        }
        //===================

        double[] pcx = new double[]{0.0623899326, 0.100000001, 0.0546918176, 0.100000001};
        double[] pcy = new double[]{-0.0326100662, -0.0403081849, 0.00499999989, 0.00499999989};
//        }

        // Here for matrix H
        double[][] mainMatrix = MatrixService.getMainMatrix(pcx, pcy);
        //-0.01666666545    0.0
        // 0.0              -0.016666667094999997
        double[][] globalAggregationH = MatrixHService.getMatrixHWithGlobalData(grid, 4);
//        double[][] matrixHForThreePointIntegration1 = MatrixHService.getMatrixHForTwoPointIntegrationWithGlobalData(grid, 3);
//        System.out.println("matrixHForThreePointIntegration1: ");
//        MatrixService.showTable2D(matrixHForThreePointIntegration1); //TODO MatrixH is always the same for every element?
//        double[][] matrixHForFourPointIntegration1 = MatrixHService.getMatrixHForTwoPointIntegrationWithGlobalData(grid, 4);
//        System.out.println("matrixHForFourPointIntegration1: ");
//        MatrixService.showTable2D(matrixHForFourPointIntegration1); //TODO MatrixH is always the same for every element?
        // Here for global aggregation matrix H
//        double[][] globalAggregationH = calculateAggregation(grid, matrixHForTwoPointIntegration1);
//        System.out.println("globalAggregationH: ");
//        MatrixService.showTable2Dshort(globalAggregationH);

//        // Here for HBC
//        double[][] globalAggregationHBC = BorderConditionService.calculateMatrixHbc(grid);
//        System.out.println("globalAggregationHBC: ");
//        MatrixService.showTable2Dshort(globalAggregationHBC);


        // *********************************************************************************************************
//        double[][] globalAggregationHplusHBC = new double[16][16];
//        for (int i = 0; i < 16; i++) {
//            for (int j = 0; j < 16; j++) {
//                globalAggregationHplusHBC[i][j] = globalAggregationHBC[i][j] + globalAggregationH[i][j];
//            }
//        }
//        System.out.println("globalAggregationHplusHBC: ");
//        MatrixService.showTable2Dshort(globalAggregationHplusHBC);
//
//
//        double[] globalAggregationVectorP = VectorP.calculateVectorP(grid);
//        System.out.println("globalAggregationVectorP: ");
//        MatrixService.showTable1D(globalAggregationVectorP);
//
//        // [H]{t}+{P}=0
//        double[] solutionForSystemOfEquations = GaussianEliminationService.findSolutionForSystemOfEquations(
//                globalAggregationHplusHBC, globalAggregationVectorP);
//        System.out.println("solutionForSystemOfEquations");
//        MatrixService.showTable1D(solutionForSystemOfEquations);
        // ************************************************************************************************************
//        // Matrix C // <----- to this point everything is good
//        double[][] globalAggregationMatrixC = MatrixC.calculateMatrixC(grid);
//        System.out.println("globalAggregationMatrixC: ");
//        MatrixService.showTable2Dshort(globalAggregationMatrixC);
//
//        // [C]/dT
//        int simulationStepTime = GlobalData.simulationStepTime;
//        double[][] globalAggregationMatrixCByDeltaTau = new double[16][16];
//        for (int i = 0; i < globalAggregationMatrixC.length; i++) {
//            for (int j = 0; j < globalAggregationMatrixC[i].length; j++) {
//                globalAggregationMatrixCByDeltaTau[i][j] = globalAggregationMatrixC[i][j] / simulationStepTime;
//                // delta tay -> simpulation step time
//            }
//        }
//        System.out.println("globalAggregationMatrixCByDeltaTau ->> [C]/dT: ");
//        MatrixService.showTable2Dshort(globalAggregationMatrixCByDeltaTau);
//
//        // Martix [H] = [H]+[C]/dT
//        double[][] globalAggregationMatrixHPlusMatrixCByDeltaTau = new double[16][16];
//        for (int i = 0; i < globalAggregationMatrixC.length; i++) {
//            for (int j = 0; j < globalAggregationMatrixC[i].length; j++) {
//                globalAggregationMatrixHPlusMatrixCByDeltaTau[i][j] = globalAggregationHplusHBC[i][j]
//                        + globalAggregationMatrixCByDeltaTau[i][j];
//            }
//        }
//        System.out.println("globalAggregationMatrixHPlusMatrixCByDeltaTau ->> Martix [H] = [H]+[C]/dT : ");
//        MatrixService.showTable2Dshort(globalAggregationMatrixHPlusMatrixCByDeltaTau);
//        double deltaT;
//        deltaT = GlobalData.simulationStepTime;
//        int iteration = 0;
//        int actualTime = 0;
//        double minimum;
//        double maximum;
//        double[] t1 = new double[16];
//
//
//        // {[C]/dT}*{T0}
//        int initialTemp = GlobalData.initialTemp;
//        double[][] globalAggregationMatrixCByDeltaTauMultiplyT0 = new double[16][16];
//        double[] CpodT = new double[16];
//        for (int i = 0; i < 16; i++) {
//            for (int j = 0; j < 16; j++) {
//                globalAggregationMatrixCByDeltaTauMultiplyT0[i][j] += globalAggregationMatrixCByDeltaTau[i][j] * initialTemp; //CG4koniec <<-----
//                CpodT[i] += globalAggregationMatrixCByDeltaTauMultiplyT0[i][j];
//            }
//        }
//
////            // FIXME doesnt work
//        // {P} = {P}+{[C]/dT}*{T0}
//        double[] vectorP_plus_matrixCByDeltaTauMultipliedByT0 = new double[16];
//        for (int i = 0; i < 16; i++) {
//            vectorP_plus_matrixCByDeltaTauMultipliedByT0[i] += globalAggregationVectorP[i] + CpodT[i];
//        }
//        System.out.println("vectorP_plus_matrixCByDeltaTauMultipliedByT0 ->> Vector ([{P}+{[C]/dT}*{T0}): ");
//        MatrixService.showTable1D(vectorP_plus_matrixCByDeltaTauMultipliedByT0);
    }


}
