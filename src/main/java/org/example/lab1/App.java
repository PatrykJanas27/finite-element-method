package org.example.lab1;

import org.example.data.InputHandler;
import org.example.lab4.MatrixHService;
import org.example.lab4.MatrixService;
import org.example.lab6.BorderConditionService;
import org.example.lab6.GaussianEliminationService;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.example.lab4.MatrixHService.globalAggregationH;
import static org.example.lab4.MatrixHService.globalAggregationMatrixC;
import static org.example.lab6.BorderConditionService.globalAggregationHBC;
import static org.example.lab6.BorderConditionService.globalAggregationVectorP;

public class App {

    private static final String FILE_NAME1 = "src/main/resources/lab1/Test1_4_4.txt";
    private static final String FILE_NAME2 = "src/main/resources/lab1/Test2_4_4_MixGrid.txt";
    private static final String FILE_NAME3 = "src/main/resources/lab1/Test3_31_31_kwadrat.txt";
    private static final int NUMBER_OF_NODES = 2;

    public static void main(String[] args) throws FileNotFoundException, NumberFormatException {
        Grid grid = InputHandler.readFile(FILE_NAME2);
        System.out.println(grid);

        // ********************** Here for calculations with time **********************
//        double currentTime = 0;
//        double[] temperaturesFromLastSimulations = new double[grid.getElementsNumber()];
//        while (GlobalData.simulationTime + GlobalData.simulationStepTime != currentTime) {
        // ********************** Here are calculation for Matrix[H] and Matrix[C] **********************
        MatrixHService.calculate_MatrixH_and_MatrixC(grid, NUMBER_OF_NODES);
        System.out.println("Global Aggregation matrix H");
        MatrixService.showTable2Dshort(globalAggregationH);
        System.out.println("Global Aggregation matrix C");
        MatrixService.showTable2Dshort(globalAggregationMatrixC);

        // ********************** Here for [HBC] and Vector {P} **********************
        BorderConditionService.calculateMatrixHbc_andVectorP(grid);
        System.out.println("globalAggregationHBC: ");
        MatrixService.showTable2Dshort(globalAggregationHBC);
        System.out.println("globalAggregationVectorP: ");
        MatrixService.showTable1D(globalAggregationVectorP);

        // ********************** Here for [H] + [HBC] **********************
        double[][] globalAggregationH_plus_HBC = new double[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationH_plus_HBC[i][j] = globalAggregationHBC[i][j] + globalAggregationH[i][j];
            }
        }
        System.out.println("globalAggregationH_plus_HBC: ");
        MatrixService.showTable2Dshort(globalAggregationH_plus_HBC);


        // ********************** Here for [H]{t}+{P}=0, where H is [H+HBC] already **********************
        double[] solutionForSystemOfEquations = GaussianEliminationService.findSolutionForSystemOfEquations(
                globalAggregationH_plus_HBC, globalAggregationVectorP);
        System.out.println("solutionForSystemOfEquations");
        MatrixService.showTable1D(solutionForSystemOfEquations);

        // ********************** Here for Matrix[C]/dT **********************
        int simulationStepTime = GlobalData.simulationStepTime;
        double[][] globalAggregationMatrixCByDeltaTau = new double[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationMatrixCByDeltaTau[i][j] = globalAggregationMatrixC[i][j] / simulationStepTime;
                // delta tau -> simulationStepTime
            }
        }
        System.out.println("globalAggregationMatrixCByDeltaTau ->> [C]/dT: ");
        MatrixService.showTable2Dshort(globalAggregationMatrixCByDeltaTau);

//            double[] initialTempVector = (currentTime == 0) ?
//                    new double[globalAggregationVectorP.length] : temperaturesFromLastSimulations;
//            if (currentTime == 0) {
//                Arrays.fill(initialTempVector, 0);
//            }

        // ********************** Here for Matrix [H] = [H]+[C]/dT **********************
        double[][] globalAggregationMatrixHPlusMatrixCByDeltaTau = new double[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationMatrixHPlusMatrixCByDeltaTau[i][j] = globalAggregationH_plus_HBC[i][j]
                        + globalAggregationMatrixCByDeltaTau[i][j];
            }
        }
        System.out.println("globalAggregationMatrixHPlusMatrixCByDeltaTau ->> Martix [H] = [H]+[C]/dT : ");
        MatrixService.showTable2Dshort(globalAggregationMatrixHPlusMatrixCByDeltaTau);

        // ********************** Here for {[C]/dT}*{T0} **********************
        int initialTemp = GlobalData.initialTemp;
        double[] globalAggregationMatrixCByDeltaTauMultiplyT0 = new double[16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalAggregationMatrixCByDeltaTauMultiplyT0[i] += globalAggregationMatrixCByDeltaTau[i][j] * initialTemp; //CG4koniec <<-----
            }
        }
        System.out.println("globalAggregationMatrixCByDeltaTauMultiplyT0 ->> Vector {[C]/dT}*{T0}: ");
        MatrixService.showTable1D(globalAggregationMatrixCByDeltaTauMultiplyT0);

        //********************** Here for {P} = {P}+{[C]/dT}*{T0} **********************
        double[] vectorP_plus_matrixCByDeltaTauMultipliedByT0 = new double[16];
        for (int i = 0; i < 16; i++) {
            //FIXME here is a bug!!!!!!!!!!!!!!!!!!!
            vectorP_plus_matrixCByDeltaTauMultipliedByT0[i] += globalAggregationVectorP[i] + globalAggregationMatrixCByDeltaTauMultiplyT0[i];
        }
        System.out.println("vectorP_plus_matrixCByDeltaTauMultipliedByT0 ->> Vector ([{P}+{[C]/dT}*{T0}): ");
        MatrixService.showTable1D(vectorP_plus_matrixCByDeltaTauMultipliedByT0);

        //********************** Adding time for solutions **********************
        double[] initialTemperatureVector = new double[grid.getNodesNumber()];
        Arrays.fill(initialTemperatureVector, GlobalData.initialTemp);
        for (int i = 0; i < grid.getNodesNumber(); i++) {
            for (int j = 0; j < grid.getNodesNumber(); j++) {
                globalAggregationH_plus_HBC[i][j] += globalAggregationMatrixC[i][j] / simulationStepTime;
            }
        }
        MatrixService.showTable2Dshort(globalAggregationH_plus_HBC);
        // *******copy******
        double[] copyOfGlobalAggregationVectorP = new double[globalAggregationVectorP.length];
        for (int i = 0; i < globalAggregationVectorP.length; i++) {
            copyOfGlobalAggregationVectorP[i] = globalAggregationVectorP[i];
        }

        double[][] copyOfGlobalAggregationH_plus_HBC = new double[globalAggregationH_plus_HBC.length][globalAggregationH_plus_HBC[0].length];
        for (int i = 0; i < globalAggregationH_plus_HBC.length; i++) {
            for (int j = 0; j < globalAggregationH_plus_HBC[i].length; j++) {
                copyOfGlobalAggregationH_plus_HBC[i][j] = globalAggregationH_plus_HBC[i][j];
            }
        }
        // after copy**********

        System.out.println("Time[s]\t\tMinTemp\t\t\t\t\tMaxTemp");
        for (int i = simulationStepTime; i <= GlobalData.simulationTime; i += simulationStepTime) {

            for (int j = 0; j < grid.getNodesNumber(); j++) {
                for (int k = 0; k < grid.getNodesNumber(); k++) {
                    globalAggregationVectorP[j] += (globalAggregationMatrixC[j][k] / simulationStepTime) * initialTemperatureVector[k];
                }
            }


            initialTemperatureVector = GaussianEliminationService.findSolutionForSystemOfEquations(globalAggregationH_plus_HBC, globalAggregationVectorP);
            for (int f = 0; f < grid.getNodesNumber(); f++) {
                globalAggregationVectorP[f] = copyOfGlobalAggregationVectorP[f];
            }
            for (int f = 0; f < grid.getNodesNumber(); f++) {
                for (int l = 0; l < grid.getNodesNumber(); l++) {
                    globalAggregationH_plus_HBC[f][l] = copyOfGlobalAggregationH_plus_HBC[f][l];
                }
            }
//            System.out.println("\ninitialTemperatureVector");
//            MatrixService.showTable1D(initialTemperatureVector);
            double min = Arrays.stream(initialTemperatureVector).min().getAsDouble();
            double max = Arrays.stream(initialTemperatureVector).max().getAsDouble();
            System.out.println(i + "\t\t\t" + min + "\t\t" + max);
        }


        // HBC += C/steptime
        //kopia vector P i HBC bo sie zmieni pozniej
        //zczytac wszystkie temperatury z pliku do wektora temperatury początkowe
        //w petli od zera do maskymalnego czasu co krok
        //vector p+=(matrixC/simulationsteptime )* vectorTemperatur[nodesize]
        //rozwiazac eleminacja gausa(borderconditionmatrixh, global vectorP)

    }


}
