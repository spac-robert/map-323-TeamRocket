package service;

import domain.User;
import repository.Repository;
import repository.database.UserRepository;
import repository.graph.Graph;
import repository.graph.UserGraph;
import repository.memory.InMemoryRepository;

import java.util.List;

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
        return graph.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        return graph.getLargestConnectedComponent().stream().toList();
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
