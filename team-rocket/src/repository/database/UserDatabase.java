package repository.database;

import domain.Utilizator;
import repository.Repository;

import java.sql.*;
import java.util.*;

public class UserDatabase implements Repository<Long, Utilizator> {
    private Connection connection;

    private void connect(String jdbcURL, String username, String password) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "require");
        connection = DriverManager.getConnection(jdbcURL, properties);
    }

    public UserDatabase(String jdbcURL, String username, String password) throws SQLException {
        connect(jdbcURL, username, password);
    }

    @Override
    public Utilizator findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Map<Long, Utilizator> users = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator user = new Utilizator(firstName, lastName);
                user.setId(id);
                users.put(id,user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users.values();
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
