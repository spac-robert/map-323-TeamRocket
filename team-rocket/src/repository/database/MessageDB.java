package repository.database;

import domain.Message;
import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class MessageDB extends InMemoryRepository<Long, Message> {
    private final Connection connection;

    public MessageDB(Connection connection, Validator<Message> validator) {
        super(validator);
        this.connection = connection;
    }


    public Message save(Message message) {
        try {
            String query = "insert into message (id_user,msg,date) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, message.getFrom().getId());
            preparedStatement.setString(2, message.getMsg());
            LocalDateTime date = message.getLocalDateTime();
            preparedStatement.setString(3, String.valueOf(date));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                message.setId(resultSet.getLong(1));
                super.save(message);
            }
            saveDestination(message.getTo(), message.getId());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    private void saveDestination(List<User> to, Long id) throws SQLException {
        String query = "insert into destination(id_user_to,id_message) values(?,?)";
        PreparedStatement preparedStatement;
        for (User user : to) {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        }
    }

}
