import domain.Utilizator;
import domain.validators.UtilizatorValidator;
import repository.Repository;
import repository.database.UserDatabase;
import repository.file.AbstractFileRepository;
import repository.file.UtilizatorFile;
import service.Service;
import service.ServiceDatabase;
import service.ServiceFile;
import ui.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static Repository<Long, Utilizator> readAccount() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data/connect-database.txt"));
        String name = reader.readLine();
        String password = reader.readLine();
        return new UserDatabase("jdbc:postgresql://localhost:5432/postgres", name, password);
    }

    public static void main(String[] args) {
        AbstractFileRepository<Long, Utilizator> repoFile = new UtilizatorFile("data/users.csv", "data/friendships.csv", new UtilizatorValidator());
        ServiceFile serviceFile = new ServiceFile(repoFile);
        try {
            Repository<Long, Utilizator> userDatabase = readAccount();
            ServiceDatabase serviceDatabase = new ServiceDatabase(userDatabase);
            Service service = new Service(serviceFile, serviceDatabase);
            UI ui = new UI(service);
            ui.menu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}