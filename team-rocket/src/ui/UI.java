package ui;

import domain.FriendRequest;
import domain.StatusFriendRequest;
import domain.User;
import service.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class UI {
    private final Service service;

    public UI(Service service) {
        this.service = service;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            menuPrint();
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> saveUI();
                case 2 -> deleteUI();
                case 3 -> printAllUI();
                case 4 -> addFriendUI();
                case 5 -> deleteFriendUI();
                case 6 -> getNrOfConnectedComponentsUI();
                case 7 -> getLargestConnectedComponentUI();
                case 8 -> updateUser();
                case 9 -> getAllFriends(scanner);
                case 10 -> getFriends(scanner);
                case 11 -> sendMsg(scanner);
                case 12 -> replyMsg(scanner);
                case 13 -> printConversation(scanner);
                case 14 -> sendFriendRequest(scanner);
                case 15 -> showNotification(scanner);
                case 16 -> acceptFriendRequest(scanner);
                case 17 -> rejectFriendRequest(scanner);
                case 0 -> loop = false;
                default -> System.out.println("Optiune inexistenta! Reincercati!");
            }
        }
    }

    private void replyMsg(Scanner input) {
        try {
            System.out.println("Give an user id: ");
            Long id = input.nextLong();
            System.out.println("Give the id of the message to reply: ");
            long idMsg = input.nextLong();
            input.nextLine();
            System.out.println("Give a message: ");
            String msg = input.nextLine();
            service.replayMsg(id, msg, idMsg);
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private void rejectFriendRequest(Scanner scanner) {
        try {
            System.out.println("Give a user id");
            long userId = scanner.nextLong();
            showNotification(userId);
            System.out.println("Give a user id");
            long acceptRequestByUserId = scanner.nextLong();
            service.updateStatusFriendRequest(userId, acceptRequestByUserId, StatusFriendRequest.REJECT);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private void acceptFriendRequest(Scanner scanner) {
        try {
            System.out.println("Give a user id");
            long userId = scanner.nextLong();
            showNotification(userId);
            System.out.println("Give a user id");
            long acceptRequestByUserId = scanner.nextLong();
            service.updateStatusFriendRequest(userId, acceptRequestByUserId, StatusFriendRequest.APPROVAL);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private void showNotification(Scanner scanner) {
        try {
            System.out.println("Give a user id");
            long userId = scanner.nextLong();
            Map<Long, FriendRequest> notifications = service.getNotifications(userId);
            for (Long key : notifications.keySet()) {
                System.out.println(notifications.get(key));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
    }

    private void showNotification(long userId) {
        try {
            Map<Long, FriendRequest> notifications = service.getNotifications(userId);
            for (Long key : notifications.keySet()) {
                System.out.println(notifications.get(key));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
    }

    private void sendFriendRequest(Scanner scanner) {
        try {
            long from;
            long to;
            System.out.println("Give a user id");
            from = scanner.nextLong();
            System.out.println("Give a user id");
            to = scanner.nextLong();
            service.sendFriendRequest(from, to);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
    }

    //TODO: implement the sql query to get all conversation between 2 users (citit baze de date)
    private void printConversation(Scanner scanner) {
        try {
            System.out.println("Give a user id: ");
            Long idUser1 = scanner.nextLong();
            System.out.println("Give a user id: ");
            Long idUser2 = scanner.nextLong();
            List<String> messages = service.getConversation(idUser1, idUser2);
            printMsg(messages);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }

    }

    private void printMsg(List<String> messages) {
        for (String msg : messages) {
            System.out.println(msg);
        }
    }


    private void sendMsg(Scanner input) {
        try {
            System.out.println("Give an user id: ");
            Long id = input.nextLong();
            input.nextLine();
            System.out.println("Give id to send: ");
            String[] tokens = input.nextLine().split(" ");
            List<Long> listOfId = new ArrayList<>();
            for (String token : tokens) {
                listOfId.add(Long.parseLong(token));
            }
            System.out.println("Give a message: ");
            String msg = input.nextLine();
            service.sendMsg(id, listOfId, msg);
        } catch (Exception e) {
            System.out.println("Invalid input");
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
        System.out.println("Give first name: ");
        String firstName = sc.nextLine();
        System.out.println("Give last name: ");
        String lastName = sc.nextLine();
        if (this.service.saveUser(firstName, lastName) != null) {
            System.out.println("User added successfully!");
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
            this.service.addFriend(id1, id2);
            System.out.println("Prietenia a fost creata!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteFriendUI() {
        printAllUI();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Give the id for the first user: ");
            Long id1 = sc.nextLong();
            System.out.println("Give the id for the second user: ");
            Long id2 = sc.nextLong();
            this.service.deleteFriend(id1, id2);
            System.out.println("Friendship was deleted");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Give the id of the deleting user: ");
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
        System.out.println("Number of community: " + nr);
    }

    private void getLargestConnectedComponentUI() {
        System.out.println("The biggest community: \n");
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
        System.out.println("9. Get all friends for a given user");
        System.out.println("10. Get all friends in a specific month");
        System.out.println("11. Send message");
        System.out.println("12. reply message");
        System.out.println("13. Display conversation between 2 users");
        System.out.println("14. Send a friend request");
        System.out.println("15 . Show notifications");
        System.out.println("16. Accept a friend request");
        System.out.println("17. Reject a friend request");
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
