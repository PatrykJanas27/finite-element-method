package org.example.lab1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalData {
    private int simulationTime;
    private int simulationStepTime;
    private int conductivity;
    private int alfa;
    private int tot;
    private int initialTemp;
    private int density;
    private int specificHeat;

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
