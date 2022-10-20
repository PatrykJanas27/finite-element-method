package org.example.lab1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    private double x;
    private double y;
    private int t;
    private boolean BC;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "\n" + "Node{" +
                "x=" + x +
                ", y=" + y +
                ", t=" + t +
                ", BC=" + BC +
                '}';
    }
}
