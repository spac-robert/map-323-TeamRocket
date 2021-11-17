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
}
