package org.example.lab1;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GlobalData {
    public static int simulationTime;
    public static int simulationStepTime;
    public static int conductivity;
    public static int alfa;
    public static int tot;
    public static int initialTemp;
    public static int density;
    public static int specificHeat;

    @Override
    public String toString() {
        return "org.example.GlobalData{" +
                "simulationTime=" + simulationTime +
                ", simulationStepTime=" + simulationStepTime +
                ", conductivity=" + conductivity +
                ", alfa=" + alfa +
                ", tot=" + tot +
                ", initialTemp=" + initialTemp +
                ", density=" + density +
                ", specificHeat=" + specificHeat +
                '}';
    }
}
