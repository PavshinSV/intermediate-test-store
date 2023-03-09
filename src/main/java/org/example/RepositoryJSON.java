package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

public class RepositoryJSON implements RepositoryInterface {
    String filenameToys;
    String filenameRaffles;
    String filenameDelivery;

    public RepositoryJSON(String filename) {
        this.filenameToys = filename;
    }

    @Override
    public ArrayList<Toy> getToys() throws RuntimeException {
        ArrayList<Toy> toys;
        try (FileReader fr = new FileReader(filenameToys); BufferedReader br = new BufferedReader(fr)) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            toys = mapper.readValue(json.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка модуля RepositoryJSON.\nПопытка распарсить строку в список.\n" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка модуля RepositoryJSON.\nПрочие ошибки при попытке прочесть файл.\n" + e.getMessage());
        }
        return toys;

    }

    @Override
    public boolean saveToys(ArrayList<Toy> toys) throws RuntimeException {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(toys);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка модуля RepositoryJSON.\nПопытка сериализации списка в строку" + e.getMessage());
        }
        try (FileWriter fr = new FileWriter(filenameToys, false)) {
            fr.write(json);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка модуля RepositoryJSON.\nПопытка записи файла" + e.getMessage());
        }
        return true;
    }

    @Override
    public Toy getToyById(int id) {
        try {
            ArrayList<Toy> toys = getToys();
            for (Toy toy : toys) {
                if (toy.id == id) {
                    return toy;
                }
            }
        } catch (RuntimeException e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean addToy(Toy toy) {
        ArrayList<Toy> toys = getToys();
        try {
            toys.add(toy);
            saveToys(toys);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removeToy(Toy toy) {
        try {
            ArrayList<Toy> toys = getToys();
            boolean result = toys.remove(toy);
            if (result) {
                saveToys(toys);
                return true;
            } else return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean updateToy(Toy toy) {
        try {
            ArrayList<Toy> toys = getToys();
            boolean result = false;
            for (int i = 0; i < toys.size(); i++) {
                if (toys.get(i).id == toy.id) {
                    toys.set(i, toy);
                    result = saveToys(toys);
                    break;
                }
            }
            return result;
        } catch (RuntimeException e) {
            return false;
        }
    }
}