import domain.User;
import domain.validators.UserValidator;
import repository.database.ConnectionDatabase;
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

    public static UserRepository<Long, User> readAccount() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader("data/database-connection.txt"));
        String name = reader.readLine();
        String password = reader.readLine();
        Connection conn = ConnectionDatabase.connect("jdbc:postgresql://localhost:5432/social_network", name, password);
        return new UserDatabase(conn, new UserValidator());
    }

    ///TODO Create a static class ConnectionDatabase that has username,password,url and will return a Connection
    ///TODO Create a class Relationship<ID> that has 2 entities
    public static void main(String[] args) {
        try {
            UserRepository<Long, User> repository = readAccount();
            Service service = new Service(repository);
            UI ui = new UI(service);
            ui.menu();
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}