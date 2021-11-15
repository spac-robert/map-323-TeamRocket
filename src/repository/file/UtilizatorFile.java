package repository.file;


import domain.Utilizator;
import domain.validators.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UtilizatorFile extends AbstractFileRepository<Long, Utilizator> {

    private static Long nextId = 0L;

    public Map<Long, Utilizator> getEntities() {
        return this.entities;
    }

    public UtilizatorFile(String fileName, String fileName2, Validator<Utilizator> validator) {
        super(fileName, fileName2, validator);
        nextId++;
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        long currentId = Long.parseLong(attributes.get(0));
        if (currentId > nextId) {
            nextId = currentId;
        }
        Utilizator u = new Utilizator(attributes.get(1), attributes.get(2));
        u.setId(Long.parseLong(attributes.get(0)));
        return u;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + '\n';
    }


    protected String createFriendshipsAsString(Utilizator entity) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(entity.getId());
        stringBuilder.append(';');
        for (Utilizator utilizator : entity.getFriends()) {
            i++;
            stringBuilder.append(utilizator.getId());
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
            Utilizator currentUtilizator = entities.get(Long.parseLong(attributes.get(0)));
            String list = attributes.get(1);
            String[] ids = list.split(",");
            for (String stringOfId : ids) {
                currentUtilizator.makeFriend(entities.get(Long.parseLong(stringOfId)));
            }
        }
    }

    @Override
    public void writeToFriendshipFile(Utilizator entity) {
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
    public void deleteOneFriend(Long id, Utilizator entity) {
        for (int i = 0; i < entity.getFriends().size(); i++) {
            Utilizator utilizator = entity.getFriends().get(i);
            if (Objects.equals(utilizator.getId(), id)) {
                List<Utilizator> list = entity.getFriends();
                list.remove(utilizator);
            }
        }
    }


    @Override
    public Utilizator save(Utilizator entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }

    public Utilizator delete(Long id) {
        Utilizator aux = super.delete(id);
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
            for (Utilizator utilizator : this.entities.values()) {
                deleteOneFriend(id, utilizator);
            }
            saveFriendsToFile();
        }
        return aux;
    }
}
