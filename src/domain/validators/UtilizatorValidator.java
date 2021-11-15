package domain.validators;

import domain.Utilizator;
import repository.Repository;

public class UtilizatorValidator implements Validator<Utilizator> {
    private Repository<Long, Utilizator> repository;

    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String message = "";
        if (entity.getFirstName().length() == 0) {
            message += "First name can't be an empty string!";
        }
        if (entity.getLastName().length() == 0) {
            message += "Last name can't be an empty string!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
