package org.example.lab2;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.pow;

public class App {
    public static void main(String[] args) {
        Function<Double, Double> uniFunction = x -> 2 * pow(x, 2) + 3 * x - 8;
        BiFunction<Double, Double, Double> biFunction = (x, y) -> -5 * pow(x, 2) * y + 2 * x * pow(y, 2) + 10;

        System.out.println("Uni Result: " + IntegrateService.calculate(uniFunction, 2));
        System.out.println("Bi Result: " + IntegrateService.calculate(biFunction, 3));

    }
}
