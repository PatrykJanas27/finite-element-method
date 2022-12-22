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
public class Element {
    private List<Integer> IDs = new ArrayList<>();

    @Override
    public String toString() {
        return "\n" + "Element{" +
                "ID=" + IDs +
                '}';
    }
}
