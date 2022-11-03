package org.example.lab3;

import org.example.lab2.IntegrateService;

import java.util.ArrayList;
import java.util.List;

public class ServicePc {
    public static double calculatePc(double x) {
        return calculateN1(x) * 2 + calculateN2(x) * 7;
    }

    public static List<Double> calculatePc(List<Double> pc){
        List<Double> resultPc = new ArrayList<>();
        for (Double aDouble : pc) {
            resultPc.add(calculatePc(aDouble));
        }
        return resultPc;
    }

    public static List<Double> calculatePc(int numberOfNodes){
        return IntegrateService.pc(numberOfNodes).stream().map(ServicePc::calculatePc).toList();
    }

    public static double calculateN1(double x) {
        return (1 - x) / 2;
    }

    public static double calculateN2(double x) {
        return (x + 1) / 2;
    }
}
