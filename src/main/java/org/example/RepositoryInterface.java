package org.example;

import java.util.ArrayList;

public interface RepositoryInterface {
    ArrayList<Toy> getToys() throws RuntimeException;
    boolean saveToys(ArrayList<Toy> toys) throws RuntimeException;
    Toy getToyById(int id);
    boolean addToy(Toy toy);
    boolean removeToy(Toy toy);
    boolean updateToy(Toy toy);
}
