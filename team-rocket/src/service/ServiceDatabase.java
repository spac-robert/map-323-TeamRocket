package service;

import domain.User;
import repository.Repository;

public class ServiceDatabase {
    private final Repository<Long, User> repository;

    public ServiceDatabase(Repository<Long, User> repository) {
        this.repository = repository;
    }

    public Iterable<User> findAllUsers() {
        return repository.findAll();
    }

    public boolean saveUser(User user) {
        return repository.save(user) != null;
    }

    public boolean deleteUser(Long idUser) {
        return repository.delete(idUser) != null;
    }


    public boolean updateUser(Long id, User updatedUser) {
        User user = repository.findOne(id);
        if (user != null) {
            updatedUser.setId(id);
            return repository.update(updatedUser) != null;
        }
        return false;
    }
}
