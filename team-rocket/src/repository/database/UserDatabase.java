package repository.database;

import domain.Utilizator;
import repository.Repository;

import java.sql.Connection;
import java.util.Properties;

public class UserDatabase implements Repository<Long, Utilizator> {
    private String jdbcURL;
    private String username;
    private String password;

    private void connect(String jdbcURL, String username, String password) {
        Properties properties = new Properties();
        properties.setProperty("user",username);
        properties.setProperty("password",password);
        properties.setProperty("ssl","true");
        ///TODO connection to database and implement the methods
        Connection connection;
    }

    public UserDatabase(String jdbcURL, String username, String password) {
        this.jdbcURL = jdbcURL;
        this.username = username;
        this.password = password;
        connect(jdbcURL, username, password);
    }

    @Override
    public Utilizator findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        return null;
    }

    @Override
    public Utilizator save(Utilizator entity) {
        return null;
    }

    @Override
    public Utilizator delete(Long aLong) {
        return null;
    }

    @Override
    public Utilizator update(Utilizator entity) {
        return null;
    }
}
