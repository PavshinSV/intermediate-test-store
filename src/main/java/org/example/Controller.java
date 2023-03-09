package org.example;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Controller {
    Model model;
    ViewInterface view;
    RepositoryInterface toysRepository;
    RepositoryInterface rafflesRepository;
    RepositoryInterface deliveryRepository;

    public Controller(Model model, ViewInterface view, RepositoryInterface toysRepository, RepositoryInterface rafflesRepository, RepositoryInterface deliveryRepository) {
        this.model = model;
        this.view = view;
        this.toysRepository = toysRepository;
        this.rafflesRepository = rafflesRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public void start() {
        modelInit();
        mainMenu();
    }

    private void mainMenu(){
        while (true){
            Scanner scanner = new Scanner(System.in);
            view.printMessage("Выберите номер действия:");
            view.printMessage("1. Отобразить список игрушек");
            view.printMessage("2. Отобразить список разыгранных игрушек");
            view.printMessage("3. Отобразить список выданых игрушек");
            view.printMessage("4. Добавить игрушку");
            view.printMessage("5. Изменить игрушку");
            view.printMessage("6. Разыграть игрушку");
            view.printMessage("7. Выдать игрушку");
            String choice = scanner.nextLine();
//            scanner.close();
            switch (choice){
                case ("1"): {
                    if (model.toyList.size()==0){
                        view.printMessage("Список пуст");
                    }
                    for (Toy toy: model.toyList){
                        view.printMessage(toy.toString());
                    }
                    break;
                }
                case ("2"):{
                    if (model.raffleToys.size()==0){
                        view.printMessage("Список пуст");
                    }
                    for (Toy toy: model.raffleToys){
                        view.printMessage(toy.toString());
                    }
                    break;
                }
                case ("3"):{
                    if (model.deliveredToys.size()==0){
                        view.printMessage("Список пуст");
                    }
                    for (Toy toy: model.deliveredToys){
                        view.printMessage(toy.toString());
                    }
                    break;
                }
                case ("4"):{
                    addToy();
                    break;
                }
                case ("5"):{
                    updateToy();
                    break;
                }
                case ("6"):{
                    rafflePrize();
                    break;
                }
                case ("7"):{
                    deliveryToy();
                    break;
                }
                default:{
                    view.printMessage("Повторите ввод");
                }
            }
        }
    }

    public void rafflePrize() {
        int i = 0;
        Random random = new Random();
        while (i<model.toyList.size()) {
            Toy toy = model.toyList.get(i);
            int chance = random.nextInt(0, 100);
            if (chance > 100-toy.pickWeight) {
                try {
                    model.raffleToys.add(toy);
                    rafflesRepository.saveToys(model.raffleToys);
                } catch (Exception e) {
                    view.printMessage(e.getMessage());
                }
                view.printMessage("Ура! Разыграна игрушка!");
                view.printMessage(toy.toString());
                return;
            }
            i++;
        }
        view.printMessage("Ни одна игрушка в этот раз ни была разыграна.\nВозможно в слудующий раз повезет больше");
    }

    public void deliveryToy() {
        while (model.raffleToys.size() > 0) {
            Toy toy = model.raffleToys.get(0);
            if (toy.amount > 0) {
                view.printMessage("Выдана игрушка: " + toy.toString());
                model.raffleToys.remove(0);
                rafflesRepository.saveToys(model.raffleToys);

                model.deliveredToys.add(toy);
                deliveryRepository.saveToys(model.deliveredToys);

                model.toyList.remove(toy);
                toy.amount -= 1;
                model.toyList.add(toy);
                toysRepository.updateToy(toy);
                return;
            } else {
                model.raffleToys.remove(0);
            }
        }
        view.printMessage("Нет разыгранных игрушек в списке для выдачи");
    }

    public void modelInit() {
        try {
            model.toyList = toysRepository.getToys();
            if (model.toyList == null) {
                fillToyList();
                toysRepository.saveToys(model.toyList);
            }
        } catch (Exception e) {
            view.printMessage(e.getMessage());
        }
        try{
            model.raffleToys = rafflesRepository.getToys();
        }catch (Exception e){
            model.raffleToys=new ArrayList<>();
            rafflesRepository.saveToys(model.raffleToys);
        }
        try{
            model.deliveredToys = deliveryRepository.getToys();
        }catch (Exception e){
            model.deliveredToys=new ArrayList<>();
            deliveryRepository.saveToys(model.deliveredToys);
        }
    }

    public void updateToy() {
        Scanner scanner = new Scanner(System.in);
        String str;
        while (true) {
            view.printMessage("Введите id игрушки: ");
            str = scanner.nextLine();
            if (isDigit(str)){
                break;
            }
            view.printMessage("Введено неверное значение");
        }
        int id = Integer.parseInt(str);
        Toy toy = toysRepository.getToyById(id);
        Toy newToy = new Toy();
        int newId;
        int pickWeight;
        int amount;
        String name;

        while (true) {
            view.printMessage("Введите целое положительное число нового id игрушки: ");
            view.printMessage("Старое значение = "+ toy.id);
            str = scanner.nextLine();
            if (isDigit(str)) {
                newId = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        while (true) {
            view.printMessage("Введите целое положительное число от 0 до 100, вероятность выпадения игрушки: ");
            view.printMessage("Старое значение = "+ toy.pickWeight);
            str = scanner.nextLine();
            if (isDigit(str)) {
                pickWeight = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        while (true) {
            view.printMessage("Введите целое положительное число, количество игрушек данного типа: ");
            view.printMessage("Старое значение = "+ toy.amount);
            str = scanner.nextLine();
            if (isDigit(str)) {
                amount = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        view.printMessage("Введите название игрушки: ");
        view.printMessage("Старое значение = "+ toy.name);
        name = scanner.nextLine();

        newToy.id = newId;
        newToy.pickWeight = pickWeight;
        newToy.amount = amount;
        newToy.name = name;

        model.toyList.remove(toy);
        model.toyList.add(newToy);
        try {
            toysRepository.saveToys(model.toyList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        scanner.close();
    }

    public void addToy() {
        int id;
        int pickWeight;
        int amount;
        String name;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            view.printMessage("Введите целое положительное число id игрушки: ");
            String str = scanner.nextLine();
            if (isDigit(str)) {
                id = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        while (true) {
            view.printMessage("Введите целое положительное число от 0 до 100, вероятность выпадения игрушки: ");
            String str = scanner.nextLine();
            if (isDigit(str)) {
                pickWeight = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        while (true) {
            view.printMessage("Введите целое положительное число, количество игрушек данного типа: ");
            String str = scanner.nextLine();
            if (isDigit(str)) {
                amount = Integer.parseInt(str);
                break;
            } else {
                view.printMessage("Введено неверное значение");
            }
        }

        view.printMessage("Введите название игрушки: ");
        name = scanner.nextLine();
        Toy toy = new Toy(id, pickWeight, amount, name);
        model.toyList.add(toy);
        toysRepository.addToy(toy);
//        scanner.close();
    }


    private void fillToyList() {
        Random random = new Random();
        model.toyList = new ArrayList<>();
        ToysNames[] toys = ToysNames.values();
        for (ToysNames toy : toys) {
            int amount = random.nextInt(0, 1000);
            int weight = random.nextInt(10, 100);
            String name = toy.name();
            int id = Math.abs(name.hashCode());
            Toy newToy = new Toy(id, weight, amount, name);
            model.toyList.add(newToy);
        }
    }

    private boolean isDigit(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
