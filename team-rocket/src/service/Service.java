package service;

import domain.Message;
import domain.User;
import repository.database.MessageDB;
import repository.database.UserRepository;
import repository.graph.Graph;
import repository.graph.UserGraph;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private final UserRepository<Long, User> userRepository;
    private final Graph<Long> graph;
    private final MessageDB messageDB;

    public Service(UserRepository<Long, User> repository, MessageDB messageDB) {
        this.userRepository = repository;
        this.graph = new UserGraph(repository);
        this.messageDB = messageDB;
    }


    public User saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        return userRepository.save(user);
    }

    public void addFriend(Long id1, Long id2, LocalDate date) {
        userRepository.addFriend(id1, id2, date);
        userRepository.addFriend(id2, id1, date);
    }

    public void deleteFriend(Long id1, Long id2) {
        userRepository.deleteFriend(id1, id2);
        userRepository.deleteFriend(id2, id1);
    }

    public User deleteUser(Long id) {
        return userRepository.delete(id);
    }

    public int getNrOfConnectedComponents() {
        graph.createGraph(userRepository);
        return graph.getNrOfConnectedComponents();
    }

    public Map<Long, User> getLargestConnectedComponent() {
        graph.createGraph(userRepository);
        Map<Long, User> mapOfUsers = new HashMap<>();
        List<Long> listOfId = graph.getLargestConnectedComponent().stream().toList();
        listOfId.forEach(id -> {
            User user = userRepository.findOne(id);
            user = userRepository.getFriend(user);
            mapOfUsers.put(user.getId(), user);
        });
        return mapOfUsers;
    }

    public User getById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> printAll() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user.setId(id);
        return userRepository.update(user);
    }

    public Map<Long, LocalDate> getFriends(Long idUser) {
        return userRepository.getFriends(idUser);
    }

    public Map<Long, LocalDate> getFriends(Long idUser, Month month) {
        Map<Long, LocalDate> friendsMap = userRepository.getFriends(idUser);
        return friendsMap.entrySet().stream()
                .filter((friendship) -> friendship.getValue().getMonth().equals(month))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void sendMsg(Long id, List<Long> listOfId, String msg) {
        User user = userRepository.findOne(id);
        List<User> users = new ArrayList<>();
        for (Long userId : listOfId) {
            users.add(userRepository.findOne(userId));
        }
        messageDB.save(new Message(user, users, msg));
    }
}