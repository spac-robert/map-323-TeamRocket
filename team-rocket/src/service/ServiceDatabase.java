package service;

import domain.Utilizator;
import repository.Repository;

public class ServiceDatabase {
    private Repository<Long, Utilizator> repository;

    public ServiceDatabase(Repository<Long, Utilizator> repository) {
        this.repository = repository;
    }
}
