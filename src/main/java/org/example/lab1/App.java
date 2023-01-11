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

        double[][] globalAggregationH = MatrixHService.getMatrixHWithGlobalData(grid, 5);


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
