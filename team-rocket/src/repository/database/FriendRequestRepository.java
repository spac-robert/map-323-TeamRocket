package repository.database;

import domain.FriendRequest;
import domain.StatusFriendRequest;
import domain.validators.FriendRequestValidation;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FriendRequestRepository extends InMemoryRepository<Long, FriendRequest> {

    private final Connection connection;

    public FriendRequestRepository(Connection connection, Validator<FriendRequest> validator) {
        super(validator);
        this.connection = connection;
        populateRepository();
    }

    public Map<Long, FriendRequest> getNotifications(long id) {
        return entities.entrySet().stream().filter(x -> x.getValue().getTo().getId().equals(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void populateRepository() {
        try {
            String query = "select * from friend_request";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long from = resultSet.getLong(2);
                long to = resultSet.getLong(3);
                String status = resultSet.getString(4);
                StatusFriendRequest request = StatusFriendRequest.toString(status);
                FriendRequest friendRequest = new FriendRequest(from, to);
                friendRequest.setId(id);
                friendRequest.setStatus(request);
                this.entities.put(id, friendRequest);
            }
        } catch (SQLException e) {
            System.out.println("Populate repository fail");
        }
    }

    public FriendRequest addFriendRequest(FriendRequest friendRequest) {
        try {
            String query = "insert into friend_request(from_user,to_user,status) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, friendRequest.getFrom().getId());
            preparedStatement.setLong(2, friendRequest.getTo().getId());
            preparedStatement.setString(3, friendRequest.getStatus());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                friendRequest.setId(friendRequest.getId());
                super.save(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequest;
    }

}