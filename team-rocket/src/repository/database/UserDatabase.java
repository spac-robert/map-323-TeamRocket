package repository.database;

import domain.User;
import domain.validators.Validator;

import java.sql.*;
import java.time.LocalDate;

public class UserDatabase extends UserRepository<Long, User> {
    private final String userId = "id";
    private final String userFirstName = "first_name";
    private final String userLastname = "last_name";
    private Connection connection;

    public UserDatabase(Connection conn, Validator<User> validator) throws SQLException {
        super(validator);
        connection = conn;
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


    public Long findOne(String firstName, String lastName) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("Select id from users where first_name=? and lastName=?");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();

            Long id = resultSet.getLong("id");
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public User getFriends(User user) {
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
                user = getFriends(user);
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
        entity.setId(findOne(entity.getFirstName(), entity.getLastName()));
        super.save(entity);
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
        super.delete(userId);
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
        super.update(entity);
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

    @Override
    public void addFriend(Long idUser1, Long idUser2, LocalDate date) {
        try {
            String query = "insert into friendship (id_user1,id_user2,date) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, idUser1);
            preparedStatement.setLong(2, idUser2);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteFriend(Long idUser1, Long idUser2) {
        try {
            String query = "delete from friendship where id_user1=? and id_user2=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, idUser1);
            preparedStatement.setLong(2, idUser2);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
