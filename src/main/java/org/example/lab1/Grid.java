package org.example.lab1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Grid {
    private int nodesNumber;
    private int elementsNumber;
    private List<Node> ND = new ArrayList<>();
    private List<Element> EL = new ArrayList<>();

    @Override
    public String toString() {
        return "Grid{" +
                "nodesNumber=" + nodesNumber +
                ", elementsNumber=" + elementsNumber +
                ", ND=" + ND +
                ", EL=" + EL +
                '}';
    }
}
