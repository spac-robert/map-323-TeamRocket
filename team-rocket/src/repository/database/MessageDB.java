package repository.database;

import domain.Message;
import domain.User;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //TODO:Make it to print the reply msg too
    public List<String> getConversation(long from, long to) {
        List<String> conversation = new ArrayList<>();
        String query = """
                select msg.id, msg.id_user, msg.msg,msg.date,msg.id_msg_reply
                from message msg,destination dest
                where (msg.id_user=? and dest.id_user_to=? and msg.id = dest.id_message)
                or (msg.id_user=? and dest.id_user_to=? and msg.id = dest.id_message)""";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, to);
            preparedStatement.setLong(3, to);
            preparedStatement.setLong(4, from);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long key = resultSet.getLong(1);
                long id_user = resultSet.getLong(2);
                String msg = resultSet.getString(3);
                String date = resultSet.getString(4);
                long id_msg_reply = resultSet.getLong(5);
                StringBuilder message = new StringBuilder();
                message.append(key).append(" | ").
                        append(id_user).append(" | ").
                        append(msg).append(" | ").
                        append(date).append(" | ").
                        append(id_msg_reply);

                conversation.add(String.valueOf(message));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return conversation;
    }

}
