package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    Model model;
    View view;
    RepositoryInterface repository;

    public Controller(Model model, View view, RepositoryInterface repository) {
        this.model = model;
        this.view = view;
        this.repository = repository;
    }

    public void start() {
        modelInit();
    }

    public void modelInit() {
        model.toyList = repository.getToys();
        if (model.toyList == null) {
            fillToyList();
            repository.saveToys(model.toyList);
        }
    }

    public void fillToyList() {
        Random random = new Random();
        model.toyList = new ArrayList<>();
        ToysNames[] toys = ToysNames.values();
        for (ToysNames toy : toys) {
            int amount = random.nextInt(1000);
            int weight = random.nextInt(10, 100);
            String name = toy.name();
            int id = name.hashCode();
            Toy newToy = new Toy(id, weight, amount, name);
            model.toyList.add(newToy);

        }
    }
}
