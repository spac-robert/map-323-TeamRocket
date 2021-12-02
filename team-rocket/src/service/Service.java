package service;

import domain.User;
import repository.database.UserRepository;
import repository.graph.Graph;
import repository.graph.UserGraph;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Service {
    private final UserRepository<Long, User> repository;
    private final Graph<Long> graph;

    public Service(UserRepository<Long, User> repository) {
        this.repository = repository;
        this.graph = new UserGraph(repository);
    }


    public User saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        return repository.save(user);
    }

    public void addFriend(Long id1, Long id2, LocalDate date) {
        repository.addFriend(id1, id2, date);
        repository.addFriend(id2, id1, date);
    }

    public void deleteFriend(Long id1, Long id2) {
        repository.deleteFriend(id1, id2);
        repository.deleteFriend(id2, id1);
    }

    public User deleteUser(Long id) {
        return repository.delete(id);
    }

    public int getNrOfConnectedComponents() {
        graph.createGraph(repository);
        return graph.getNrOfConnectedComponents();
    }

    public Map<Long, User> getLargestConnectedComponent() {
        graph.createGraph(repository);
        Map<Long, User> mapOfUsers = new HashMap<>();
        List<Long> listOfId = graph.getLargestConnectedComponent().stream().toList();
        listOfId.forEach(id -> {
            User user = repository.findOne(id);
            user = repository.getFriend(user);
            mapOfUsers.put(user.getId(), user);
        });
        return mapOfUsers;
    }

    public User getById(Long id) {
        return repository.findOne(id);
    }

    public Iterable<User> printAll() {
        return repository.findAll();
    }

    public User updateUser(Long id, String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user.setId(id);
        return repository.update(user);
    }

    public Map<Long, LocalDate> getFriends(Long idUser) {
        return repository.getFriends(idUser);
    }

    public Map<Long, LocalDate> getFriends(Long idUser, Month month) {
        Map<Long, LocalDate> friendsMap = repository.getFriends(idUser);
        return friendsMap.entrySet().stream().filter(map -> map.getValue().getMonth().equals(month)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}