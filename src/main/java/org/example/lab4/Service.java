package org.example.lab4;

public class Service {
    public static double calculateKsi1(double ksi){
        return -0.25*(1-ksi);
    }

    public static double calculateKsi2(double ksi){
        return 0.25*(1-ksi);
    }

    public static double calculateEta1(double eta){
        return -0.25*(1-eta);
    }

    public static double calculateEta2(double eta){
        return 0.25*(1-eta);
    }

}
