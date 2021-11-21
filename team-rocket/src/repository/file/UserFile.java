package repository.file;


import domain.User;
import domain.validators.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserFile extends AbstractFileRepository<Long, User> {

    private static Long nextId = 0L;

    public Map<Long, User> getEntities() {
        return this.entities;
    }

    public UserFile(String fileName, String fileName2, Validator<User> validator) {
        super(fileName, fileName2, validator);
        nextId++;
    }

    @Override
    public User extractEntity(List<String> attributes) {
        long currentId = Long.parseLong(attributes.get(0));
        if (currentId > nextId) {
            nextId = currentId;
        }
        User u = new User(attributes.get(1), attributes.get(2));
        u.setId(Long.parseLong(attributes.get(0)));
        return u;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + '\n';
    }


    protected String createFriendshipsAsString(User entity) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(entity.getId());
        stringBuilder.append(';');
        for (User user : entity.getFriends()) {
            i++;
            stringBuilder.append(user.getId());
            if (i != entity.getFriends().size()) {
                stringBuilder.append(',');
            }
        }
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    @Override
    protected void makeFriendships(List<String> attributes) {
        if (attributes.size() == 2) {
            User currentUser = entities.get(Long.parseLong(attributes.get(0)));
            String list = attributes.get(1);
            String[] ids = list.split(",");
            for (String stringOfId : ids) {
                currentUser.makeFriend(entities.get(Long.parseLong(stringOfId)));
            }
        }
    }

    @Override
    public void writeToFriendshipFile(User entity) {
        FileWriter file;
        try {
            file = new FileWriter(this.fileName2, true);
            file.write(createFriendshipsAsString(entity));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOneFriend(Long id, User entity) {
        for (int i = 0; i < entity.getFriends().size(); i++) {
            User user = entity.getFriends().get(i);
            if (Objects.equals(user.getId(), id)) {
                List<User> list = entity.getFriends();
                list.remove(user);
            }
        }
    }


    @Override
    public User save(User entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }

    public User delete(Long id) {
        User aux = super.delete(id);
        if (aux != null) {
            //clear file
            try {
                new FileWriter(this.fileName).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //append to file
            List<Long> keyList = new ArrayList<>(entities.keySet());
            for (Long key : keyList) {
                writeToFile(entities.get(key));
            }
            for (User user : this.entities.values()) {
                deleteOneFriend(id, user);
            }
            saveFriendsToFile();
        }
        return aux;
    }
}
