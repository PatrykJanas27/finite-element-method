package org.example.lab4;

import org.example.lab2.IntegrateService;

import java.util.List;

import static java.lang.Math.pow;

public class App {
    public static void main(String[] args) {
        List<Double> pc1 = IntegrateService.pc(2);
        List<Double> w1 = IntegrateService.w(2);

        List<Double> pc2 = IntegrateService.pc(3);
        List<Double> w2 = IntegrateService.w(3);

        double[][] tableKsi4 =  new double[4][4];

        for (int i = 0; i < 4; i++) {
                    tableKsi4[i][0] =  Service.calculateKsi1(-1 / (pow(3, 0.5)));
                    tableKsi4[i][1] =  Service.calculateKsi2(-1 / (pow(3, 0.5)));
                    tableKsi4[i][2] =  Service.calculateKsi2(1 / (pow(3, 0.5)));
                    tableKsi4[i][3] =  Service.calculateKsi1(1 / (pow(3, 0.5)));
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("[i:"+i + "][j:" + j + "] " + tableKsi4[i][j]);
            }
        }

        System.out.println("Eta:");
        double[][] tableEta4 =  new double[4][4];

        for (int i = 0; i < 4; i++) {
            tableEta4[i][0] =  Service.calculateEta1(-1 / (pow(3, 0.5)));
//            tableEta4[i][1] =  Service.calculateEta1(1 / (pow(3, 0.5)));
            tableEta4[i][1] =  Service.calculateEta1(-1 / (pow(3, 0.5)));
//            tableEta4[i][3] =  Service.calculateEta2(1 / (pow(3, 0.5)));
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("[i:"+i + "][j:" + j + "] " + tableEta4[i][j]);
            }
        }

    }
}
