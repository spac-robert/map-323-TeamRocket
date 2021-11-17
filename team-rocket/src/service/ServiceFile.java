package service;

import domain.User;
import repository.file.AbstractFileRepository;
import repository.graph.Graph;

import java.util.*;

public class ServiceFile {
    private final AbstractFileRepository<Long, User> repository;

    public ServiceFile(AbstractFileRepository<Long, User> repository) {
        this.repository = repository;
    }

    public void saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        this.repository.save(user);
    }

    public void deleteUser(Long id) {
        try {
            this.repository.delete(id);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }


    public Iterable<User> printAll() {
        return this.repository.findAll();
    }

    public void addFriend(Long id1, Long id2) {
        try {
            boolean ok = true;
            if (!Objects.equals(id1, id2)) {
                User user1 = this.repository.findOne(id1);
                User user2 = this.repository.findOne(id2);
                for (User user : user1.getFriends()) {
                    if (Objects.equals(user.getId(), id2)) {
                        ok = false;
                        break;
                    }
                }
                for (User user : user2.getFriends()) {
                    if (Objects.equals(user.getId(), id1)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    user1.makeFriend(user2);
                    user2.makeFriend(user1);
                    this.repository.saveFriendsToFile();
                }
            }
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public void deleteFriend(Long id1, Long id2) {
        User user = this.repository.findOne(id1);
        User user1 = this.repository.findOne(id2);
        this.repository.deleteOneFriend(id2, user);
        this.repository.deleteOneFriend(id1, user1);
        this.repository.saveFriendsToFile();
    }

    public int getNrOfConnectedComponents() {
        Graph graph = new Graph(this.repository);
        return graph.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        Graph graph = new Graph(this.repository);
        return graph.getLargestConnectedComponent().stream().toList();
    }

    public User getById(Long x) {
        return this.repository.findOne(x);
    }
}
