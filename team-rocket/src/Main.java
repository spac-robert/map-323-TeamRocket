import domain.User;
import domain.validators.FriendRequestValidation;
import domain.validators.MessageValidator;
import domain.validators.UserValidator;
import repository.database.*;
import service.Service;
import ui.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class Main {

    public static Connection readAccount() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader("data/database-connection.txt"));
        String name = reader.readLine();
        String password = reader.readLine();
        return ConnectionDatabase.connect("jdbc:postgresql://localhost:5432/social_network", name, password);
    }

    public static void main(String[] args) {
        try {
            Connection connection = readAccount();
            UserRepository<Long, User> repository = new UserDatabase(connection, new UserValidator());
            MessageDB messageDB = new MessageDB(connection, new MessageValidator());
            FriendRequestRepository friendRequestRepository = new FriendRequestRepository(connection, new FriendRequestValidation());
            Service service = new Service(repository, friendRequestRepository, messageDB);
            UI ui = new UI(service);
            ui.menu();
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}