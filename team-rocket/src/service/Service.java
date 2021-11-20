package service;

import domain.User;
import repository.Repository;
import repository.graph.Graph;
import repository.graph.UserGraph;
import repository.memory.InMemoryRepository;

import java.util.List;

public class Service {
    private final Repository<Long, User> repository;
    ///TODO Create a repository for Friendship and that extend an InMemoryRepository as a list
    private final Graph<Long> graph;

    public Service(Repository<Long, User> repository) {
        this.repository = repository;
        this.graph = new UserGraph((InMemoryRepository<Long, User>) repository);
    }


    public User saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        return repository.save(user);
    }

    ///TODO Think how to implement add and delete friend methods
    public void addFriend(Long id1, Long id2) {
        //  serviceFile.addFriend(id1, id2);
    }

    public void deleteFriend(Long id1, Long id2) {
        // serviceFile.deleteFriend(id1, id2);
    }

    public User deleteUser(Long id) {
        return repository.delete(id);
    }

    public int getNrOfConnectedComponents() {
        return graph.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        return graph.getLargestConnectedComponent().stream().toList();
        ///TODO For each id from getLargestConnectedComponent gets the user and add all friends
        // for it, and then add to a List<Users> must change te return statement this method
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
