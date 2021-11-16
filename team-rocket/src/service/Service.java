package service;

import domain.Utilizator;

import java.util.List;

public class Service {
    private ServiceFile serviceFile;
    private ServiceDatabase serviceDatabase;

    public Service(ServiceFile serviceFile, ServiceDatabase serviceDatabase) {
        this.serviceFile = serviceFile;
        this.serviceDatabase = serviceDatabase;
    }


    public void saveUtilizator(String firstName, String lastName) {
        serviceFile.saveUtilizator(firstName, lastName);
    }

    public void addFriend(Long id1, Long id2) {
        serviceFile.addFriend(id1, id2);
    }

    public void deleteFriend(Long id1, Long id2) {
        serviceFile.deleteFriend(id1, id2);
    }

    public void deleteUtilizator(Long id) {
        serviceFile.deleteUtilizator(id);
    }

    public int getNrOfConnectedComponents() {
        return serviceFile.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        return serviceFile.getLargestConnectedComponent();
    }

    public Utilizator getById(Long id) {
        return serviceFile.getById(id);
    }

    public Iterable<Utilizator> printAll() {
        return serviceFile.printAll();
    }

    public Iterable<Utilizator> printAllUsersFromDB() {
        return serviceDatabase.findAllUsers();
    }

}
