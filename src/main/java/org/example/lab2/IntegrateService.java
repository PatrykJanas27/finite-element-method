package org.example.lab2;

import org.example.lab1.GlobalData;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Deprecated
@SuppressWarnings("DuplicatedCode")
public class IntegrateService {

    public static double calculate(Function<Double, Double> uniFunction, int numberOfNodes) {
        List<Double> pc = GlobalData.getPcList(numberOfNodes);
        List<Double> weights = GlobalData.getWeightsList(numberOfNodes);
        double result = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            result += uniFunction.apply(pc.get(i)) * weights.get(i);
        }
        return result;
    }

    public static double calculate(Function<Double, Double> uniFunction, List<Double> pc, List<Double> weights, int numberOfNodes) {
        if (pc.size() != numberOfNodes || weights.size() != numberOfNodes) {
            throw new IllegalArgumentException("Size of pcList or wList has to be compatible with numberOfNodes");
        }
        double result = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            result += uniFunction.apply(pc.get(i)) * weights.get(i);
        }
        return result;
    }

    public static double calculate(BiFunction<Double, Double, Double> biFunction, int numberOfNodes) {
        List<Double> pc = GlobalData.getPcList(numberOfNodes);
        List<Double> w = GlobalData.getWeightsList(numberOfNodes);
        double result = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                result += biFunction.apply(pc.get(i), pc.get(j)) * w.get(i) * w.get(j);
            }
        }
        return result;
    }

    public static double calculate(BiFunction<Double, Double, Double> biFunction, List<Double> pc, List<Double> w, int numberOfNodes) {
        if (pc.size() != numberOfNodes || w.size() != numberOfNodes) {
            throw new IllegalArgumentException("Size of pcList or wList has to be compatible with numberOfNodes");
        }
        double result = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                result += biFunction.apply(pc.get(i), pc.get(j)) * w.get(i) * w.get(j);
            }
        }
        return result;
    }

}



