package org.example.lab3;

import org.example.lab2.IntegrateService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class App {
    public static void main(String[] args) {

        List<Double> pc = new ArrayList<>();
        pc.add(calculatePc(-1/sqrt(3)));
        pc.add(calculatePc(1/sqrt(3)));

        List<Double> w = new ArrayList<>();
        w.add(2.5);
        w.add(2.5);

        Function<Double, Double> uniFunction = x -> pow(x, 2) + 3 * x + 2;
        System.out.println(IntegrateService.calculate(uniFunction,pc,w, 2));
    }

    private static double calculatePc(double x){
        return calculateN1(x) * 2 + calculateN2(x) *7;
    }

    private static double calculateN1(double x){
        return (1-x)/2;
    }

    private static double calculateN2(double x){
        return (x+1)/2;
    }

}
