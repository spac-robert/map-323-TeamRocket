package domain.validators;

import domain.User;
import repository.Repository;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
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
