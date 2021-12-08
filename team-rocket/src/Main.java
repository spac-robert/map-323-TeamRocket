import domain.Message;
import domain.User;
import domain.validators.MessageValidator;
import domain.validators.UserValidator;
import domain.validators.Validator;
import repository.database.ConnectionDatabase;
import repository.database.MessageDB;
import repository.database.UserDatabase;
import repository.database.UserRepository;
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
        Connection conn = ConnectionDatabase.connect("jdbc:postgresql://localhost:5432/social_network", name, password);
        return conn;
    }

    public static void main(String[] args) {
        try {
            Connection connection = readAccount();
            UserRepository<Long, User> repository = new UserDatabase(connection,new UserValidator());
            MessageDB messageDB = new MessageDB(connection, new MessageValidator());
            Service service = new Service(repository,messageDB);
            UI ui = new UI(service);
            ui.menu();
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}