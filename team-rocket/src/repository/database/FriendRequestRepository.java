package repository.database;

import domain.FriendRequest;
import domain.validators.FriendRequestValidation;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRequestRepository extends InMemoryRepository<Long, FriendRequest> {

    private final Connection connection;

    public FriendRequestRepository(Connection connection, Validator<FriendRequest> validator) {
        super(validator);
        this.connection = connection;
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