package org.example.lab2;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.pow;

public class App {
    public static void main(String[] args) {
        Function<Double, Double> uniFunction = x -> 2 * pow(x, 2) + 3 * x - 8;
        BiFunction<Double, Double, Double> biFunction = (x, y) -> -5 * pow(x, 2) * y + 2 * x * pow(y, 2) + 10;

        showResult(uniFunction);
        showResult(biFunction);

    }

    private static void showResult(Function<Double, Double> uniFunction) {
        System.out.println("Uni Result for n = 2: " + IntegrateService.calculate(uniFunction, 2));
        System.out.println("Uni Result for n = 3: " + IntegrateService.calculate(uniFunction, 3));
        System.out.println("Uni Result for n = 4: " + IntegrateService.calculate(uniFunction, 4));
        System.out.println("Uni Result for n = 5: " + IntegrateService.calculate(uniFunction, 5));
    }

    private static void showResult(BiFunction<Double, Double, Double> biFunction) {
        for (int i = 2; i <= 5; i++) {
            System.out.println("Uni Result for n = " + i + ": " + IntegrateService.calculate(biFunction, i));
        }
    }
}
