package repository.database;

import domain.Entity;
import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public abstract class UserRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    public UserRepository(Validator<E> validator) {
        super(validator);
    }

    public abstract void addFriend(ID idUser1, ID idUser2, LocalDate date);

    public abstract void deleteFriend(ID id_user1, ID id_user2);

    public abstract E getFriend(E user);

    public abstract Map<ID, LocalDate> getFriends(ID iddUser);

}
