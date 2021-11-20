package repository.database;

import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.*;

public class UserDatabase extends InMemoryRepository<Long, User> {
    private final String userId = "id";
    private final String userFirstName = "first_name";
    private final String userLastname = "last_name";
    private Connection connection;

    private void connect(String jdbcURL, String username, String password) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "require");
        connection = DriverManager.getConnection(jdbcURL, properties);
    }

    public UserDatabase(String jdbcURL, String username, String password, Validator<User> validator) throws SQLException {
        super(validator);
        connect(jdbcURL, username, password);
        populateRepository();
    }

    @Override
    public User findOne(Long idFriend) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("Select first_name,last_name from users where id=" + idFriend);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            User user = new User(firstName, lastName);
            user.setId(idFriend);
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private User addFriendship(User user) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT id_user2 from friendship where id_user1=" + user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long friend = resultSet.getLong("id_user2");
                user.makeFriend(findOne(friend));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong(userId);
                String firstName = resultSet.getString(userFirstName);
                String lastName = resultSet.getString(userLastname);
                User user = new User(firstName, lastName);
                user.setId(id);
                user = addFriendship(user);
                entities.put(id, user);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entities.values();
    }

    @Override
    public User save(User entity) {
        User user = null;
        try {
            String query = "insert into users (first_name,last_name) values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.execute();
            user = entity;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public User delete(Long userId) {
        User user = findOne(userId);
        try {
            String query = "delete from users where id=" + userId;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public User update(User entity) {
        User user = null;
        try {
            String query = "update users set first_name=?,last_name=? where id=" + entity.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.execute();
            user = entity;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    private void populateRepository() {
        findAll();
    }
}
