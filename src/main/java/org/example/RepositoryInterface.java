package org.example;

import java.util.ArrayList;

public interface RepositoryInterface {
    ArrayList<Toy> getToys();
    boolean saveToys(ArrayList<Toy> toys);
    Toy getToyById(int id);
    boolean addToy(Toy toy);
}
