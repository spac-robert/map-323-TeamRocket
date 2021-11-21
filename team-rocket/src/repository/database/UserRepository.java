package repository.database;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

public abstract class UserRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    public UserRepository(Validator<E> validator) {
        super(validator);
    }

    public abstract void addFriend(ID idUser1, ID idUser2);

    public abstract void deleteFriend(ID id_user1, ID id_user2);
}
