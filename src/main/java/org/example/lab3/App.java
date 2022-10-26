package org.example.lab3;

import org.example.lab2.IntegrateService;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;
import static org.example.lab3.ServicePc.calculatePc;
import static org.example.lab3.ServiceW.calculateW;

public class App {
    public static void main(String[] args) {
        Function<Double, Double> uniFunction1 = x -> x + 3;
        Function<Double, Double> uniFunction2 = x -> pow(x, 2) + 3 * x + 2;
        double x1 = 2;
        double x2 = 7;
//        IntegrateService.calculate(uniFunction, pc, w, numberOfNodes)

        System.out.println("First function");
        showResult(uniFunction1, x1, x2);
        System.out.println("Second function");
        showResult(uniFunction2, x1, x2);
    }

    private static void showResult(Function<Double, Double> uniFunction, double x1, double x2) {
        for (int numberOfNodes = 2; numberOfNodes <= 5; numberOfNodes++) {
            List<Double> pc = calculatePc(numberOfNodes);
            List<Double> w = calculateW(x1, x2, numberOfNodes);
            System.out.print("Uni Result for n = " + numberOfNodes + ": ");
            System.out.println(IntegrateService.calculate(uniFunction, pc, w, numberOfNodes));
        }
    }


}
