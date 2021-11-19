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


    public boolean saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        serviceFile.saveUser(user);
        return serviceDatabase.saveUser(user);

    }

    public void addFriend(Long id1, Long id2) {
        serviceFile.addFriend(id1, id2);
    }

    public void deleteFriend(Long id1, Long id2) {
        serviceFile.deleteFriend(id1, id2);
    }

    public boolean deleteUser(Long id) {
        //serviceFile.deleteUser(id);
        return serviceDatabase.deleteUser(id);
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

    public boolean updateUser(Long id, String firstName, String lastName) {
        User user = new User(firstName, lastName);
        return serviceDatabase.updateUser(id, user);
    }

}
