package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String fileName;
    protected String fileName2;

    public AbstractFileRepository(String fileName, String fileName2, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        this.fileName2 = fileName2;
        loadData();
        loadFriendships();
    }

    private void loadData() {
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bf.readLine()) != null) {
                E e = extractEntity(Arrays.asList(line.split(";")));
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFriendships() {
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName2))) {
            String line;
            while ((line = bf.readLine()) != null) {
                makeFriendships(Arrays.asList(line.split(";")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes list of string with attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    protected abstract void makeFriendships(List<String> attributes);

    public abstract void writeToFriendshipFile(E entity);

    public abstract void deleteOneFriend(ID id, E entity);

    @Override
    public E save(E entity) {
        E aux = super.save(entity);
        if (aux == null) {
            writeToFile(entity);
            writeToFriendshipFile(entity);
        }
        return aux;
    }

    public E delete(ID id) {
        E entity = entities.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }
        return entities.remove(id);
    }

    public void writeToFile(E entity) {
        FileWriter file = null;
        try {
            file = new FileWriter(this.fileName, true);
            file.write(createEntityAsString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert file != null;
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriendsToFile() {

        //clear file
        try {
            new FileWriter(this.fileName2).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //append to file
        ArrayList<ID> keyList = new ArrayList<>(entities.keySet());
        for (ID key : keyList) {
            writeToFriendshipFile(entities.get(key));
        }
    }
}


