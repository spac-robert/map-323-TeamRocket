package repository.database;

import domain.User;
import repository.Repository;

import java.sql.*;
import java.util.*;

public class UserDatabase implements Repository<Long, User> {
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

    public UserDatabase(String jdbcURL, String username, String password) throws SQLException {
        connect(jdbcURL, username, password);
    }


    @Override
    public User findOne(Long idFriend) {
        PreparedStatement preparedStatement = null;
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
            e.printStackTrace();
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
        Map<Long, User> users = new HashMap<>();
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
                users.put(id, user);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users.values();
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public User delete(Long aLong) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }
}
