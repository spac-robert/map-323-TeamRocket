import domain.Utilizator;
import domain.validators.UtilizatorValidator;
import repository.file.AbstractFileRepository;
import repository.file.UtilizatorFile;
import service.Service;
import ui.UI;

public class Main {
    public static void main(String[] args) {
        AbstractFileRepository<Long, Utilizator> repoFile = new UtilizatorFile("data/users.csv", "data/friendships.csv", new UtilizatorValidator());
        Service service = new Service(repoFile);
        UI ui = new UI(service);
        ui.menu();
    }
}