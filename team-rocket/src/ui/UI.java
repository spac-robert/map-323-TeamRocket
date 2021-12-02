package ui;

import domain.User;
import service.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private final Service service;

    public UI(Service service) {
        this.service = service;
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            menuPrint();
            int option = sc.nextInt();
            switch (option) {
                case 1 -> saveUI();
                case 2 -> deleteUI();
                case 3 -> printAllUI();
                case 4 -> addFriendUI();
                case 5 -> deleteFriendUI();
                case 6 -> getNrOfConnectedComponentsUI();
                case 7 -> getLargestConnectedComponentUI();
                case 8 -> updateUser();
                case 9 -> getAllFriends(sc);
                case 10 -> getFriends(sc);
                case 0 -> loop = false;
                default -> System.out.println("Optiune inexistenta! Reincercati!");
            }
        }
    }

    private void getFriends(Scanner input) {
        try {
            System.out.println("Give an user id: ");
            Long id = input.nextLong();
            System.out.println("Give a month: ");
            Month month = Month.of(Integer.parseInt(input.next()));
            Map<Long, LocalDate> friendsMap = service.getFriends(id, month);
            if (friendsMap.isEmpty())
                System.out.println("No friendship at this month " + month);
            else {
                for (Long key : friendsMap.keySet()) {
                    System.out.println(service.getById(key) + "|" + friendsMap.get(key));
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private void saveUI() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dati prenumele si numele utilizatorului de adaugat: ");
        String firstName = sc.nextLine();
        String lastName = sc.nextLine();
        if (this.service.saveUser(firstName, lastName) != null) {
            System.out.println("Utilizatorul a fost adaugat!");
        } else {
            System.out.println("User already exists");
        }
    }

    private void addFriendUI() {
        printAllUI();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Dati id-ul primului utilizator: ");
            Long id1 = sc.nextLong();
            System.out.println("Dati id-ul celui de al doilea utilizator: ");
            Long id2 = sc.nextLong();
            LocalDate date = LocalDate.now();
            this.service.addFriend(id1, id2, date);
            System.out.println("Prietenia a fost creata!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteFriendUI() {
        printAllUI();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Dati id-ul primului utilizator: ");
            Long id1 = sc.nextLong();
            System.out.println("Dati id-ul celui de al doilea utilizator: ");
            Long id2 = sc.nextLong();
            this.service.deleteFriend(id1, id2);
            System.out.println("Prietenia a fost stearsa!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul utilizatorului de sters: ");
            Long id = sc.nextLong();
            if (this.service.deleteUser(id) != null) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("User couldn't be deleted");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getNrOfConnectedComponentsUI() {
        int nr = this.service.getNrOfConnectedComponents();
        System.out.println("Numarul de comunitati este: " + nr);
    }

    private void getLargestConnectedComponentUI() {
        System.out.println("Cea mai sociabila comunitate este: \n");
        Map<Long, User> largestConnectedComponent = this.service.getLargestConnectedComponent();
        for (Long x : largestConnectedComponent.keySet()) {
            System.out.println(largestConnectedComponent.get(x));
        }
    }

    private void printAllUI() {
        Iterable<User> users = this.service.printAll();
        users.forEach(System.out::println);
    }

    private void menuPrint() {
        System.out.println("-----MENU-----");
        System.out.println("Select an option: ");
        System.out.println("0. Exit");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Display all users");
        System.out.println("4. Add friendship between 2 users");
        System.out.println("5. Delete friend");
        System.out.println("6. Determinate number of connected components");
        System.out.println("7. Determinate the biggest connected component");
        System.out.println("8. Update users from db");
        System.out.println("-----------------------");
    }

    private void updateUser() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Give the user's id to update:");
            Long id1 = sc.nextLong();
            System.out.println("First name: ");
            String firstName = sc.next();
            System.out.println("Last name: ");
            String lastName = sc.next();
            if (this.service.updateUser(id1, firstName, lastName) != null) {
                System.out.println("User updated successfully");
            } else {
                System.out.println("Couldn't update the user");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getAllFriends(Scanner input) {
        System.out.println("Give an id: ");
        Long iddUser = input.nextLong();
        Map<Long, LocalDate> friendsMap = service.getFriends(iddUser);
        for (Long key : friendsMap.keySet()) {
            System.out.println(service.getById(key) + "|" + friendsMap.get(key));
        }
    }
}
