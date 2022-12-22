package org.example.lab1;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Grid {
    private int nodesNumber;
    private int elementsNumber;
    private List<Node> nodes = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    @Override
    public String toString() {
        return "Grid{" +
                "nodesNumber=" + nodesNumber +
                ", elementsNumber=" + elementsNumber +
                ", ND=" + nodes +
                ", EL=" + elements +
                '}';
    }

}
