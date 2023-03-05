package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

public class RepositoryJSON implements RepositoryInterface {
    String filename;

    public RepositoryJSON(String filename) {
        this.filename = filename;
    }

    @Override
    public ArrayList<Toy> getToys() {
        ArrayList<Toy> toys;
        try (FileReader fr = new FileReader(filename); BufferedReader br = new BufferedReader(fr)) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            toys = mapper.readValue(json.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка модуля RepositoryJSON");
            System.out.println("Попытка распарсить строку в список");
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка модуля RepositoryJSON");
            System.out.println("Прочие ошибки при попытке прочесть файл");
            System.out.println(e.getMessage());
            return null;
        }
        return toys;

    }

    @Override
    public boolean saveToys(ArrayList<Toy> toys) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(toys);
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка модуля RepositoryJSON");
            System.out.println("Попытка сериализации списка в строку");
            System.out.println(e.getMessage());
            return false;
        }
        try (FileWriter fr = new FileWriter(filename, false)) {
            fr.write(json);
        } catch (IOException e) {
            System.out.println("Ошибка модуля RepositoryJSON");
            System.out.println("Попытка записи файла");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Toy getToyById(int id) {
        //Реализация в JSON неподразумевается
        return null;
    }

    @Override
    public boolean addToy(Toy toy) {
        //Реализация в JSON неподразумевается
        return false;
    }

}