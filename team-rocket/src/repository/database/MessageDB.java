package repository.database;

import domain.*;
import domain.validators.UserValidator;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageDB extends InMemoryRepository<Long, Message> {
    private final Connection connection;
    private UserDatabase userDatabase;

    public MessageDB(Connection connection, Validator<Message> validator) {
        super(validator);
        this.connection = connection;
        this.userDatabase = new UserDatabase(connection, new UserValidator());
        populateRepository();
    }

    private void populateRepository() {
        try {
            String query = "select * from message";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long id_user = resultSet.getLong(2);
                String msg = resultSet.getString(3);
                Message replyMessage = new Message(userDatabase.findOne(id_user), msg);
                replyMessage.setId(id);
                this.entities.put(id, replyMessage);
            }
        } catch (SQLException e) {
            System.out.println("Populate repository fail");
        }
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
                message.setLocalDateTime(date);
                message.setId(resultSet.getLong(1));
                super.save(message);
            }
            saveDestination(message.getTo(), message.getId());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }


    public void saveDestination(List<User> to, Long id) {
        try {
            String query = "insert into destination(id_user_to,id_message) values(?,?)";
            PreparedStatement preparedStatement;
            for (User user : to) {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, id);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

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

    public List<Long> getAllGroup(long from, long replyMsg) {
        List<Long> friendsId = new ArrayList<>();
        try {
            String query = "select distinct id_user_to from destination where id_message=? and id_user_to!=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, replyMsg);
            statement.setLong(2, from);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                friendsId.add(set.getLong(1));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return friendsId;
    }

    public ReplyMessage save(ReplyMessage message) {
        try {
            String query = "insert into message (id_user,msg,date,id_msg_reply) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, message.getMessage().getFrom().getId());
            preparedStatement.setString(2, message.getMessage().getMsg());
            LocalDateTime date = message.getMessage().getLocalDateTime();
            preparedStatement.setString(3, String.valueOf(date));
            preparedStatement.setLong(4, message.getIdMsgToReply());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                message.getMessage().setLocalDateTime(date);
                message.setId(resultSet.getLong(1));
                super.save(message.getMessage());
            }
            saveDestination(message.getMessage().getTo(), message.getId());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }
}
