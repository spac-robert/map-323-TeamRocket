package service;

import domain.User;
import repository.database.UserRepository;
import repository.graph.Graph;
import repository.graph.UserGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addFriend(Long id1, Long id2) {
        repository.addFriend(id1, id2);
        repository.addFriend(id2, id1);
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
            user = repository.getFriends(user);
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
}