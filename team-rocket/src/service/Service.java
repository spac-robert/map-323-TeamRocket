package service;

import domain.User;

import java.util.List;

public class Service {
    private final ServiceFile serviceFile;
    private final ServiceDatabase serviceDatabase;

    public Service(ServiceFile serviceFile, ServiceDatabase serviceDatabase) {
        this.serviceFile = serviceFile;
        this.serviceDatabase = serviceDatabase;
    }


    public void saveUser(String firstName, String lastName) {
        serviceFile.saveUser(firstName, lastName);
    }

    public void addFriend(Long id1, Long id2) {
        serviceFile.addFriend(id1, id2);
    }

    public void deleteFriend(Long id1, Long id2) {
        serviceFile.deleteFriend(id1, id2);
    }

    public void deleteUser(Long id) {
        serviceFile.deleteUser(id);
    }

    public int getNrOfConnectedComponents() {
        return serviceFile.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        return serviceFile.getLargestConnectedComponent();
    }

    public User getById(Long id) {
        return serviceFile.getById(id);
    }

    public Iterable<User> printAll() {
        return serviceFile.printAll();
    }

    public Iterable<User> printAllUsersFromDB() {
        return serviceDatabase.findAllUsers();
    }

}
