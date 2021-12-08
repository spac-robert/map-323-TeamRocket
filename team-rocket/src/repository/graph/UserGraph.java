package repository.graph;

import domain.User;
import repository.memory.InMemoryRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserGraph extends AbstractGraph<Long> {
    protected List<Long> keyList;

    public UserGraph(InMemoryRepository<Long, User> repository) {
        createGraph(repository);
    }

    @Override
    public void createGraph(InMemoryRepository<Long, User> repository) {
        keyList = new ArrayList<>(repository.getEntities().keySet());
        for (Long key : keyList) {
            addVertex(key);
        }
        for (User user : repository.getEntities().values()) {
            List<User> friends = user.getFriends();
            for (User user1 : friends) {
                addEdge(user.getId(), user1.getId());
            }
        }
    }

    @Override
    public Set<Set<Long>> poolOfConnectedComponents() {
        Set<Set<Long>> stringSet = new HashSet<>();
        ArrayList<String> stringBuilder = new ArrayList<>();
        for (Long key : keyList) {
            stringBuilder.add(key.toString());
        }
        for (String s : stringBuilder) {
            stringSet.add(this.breadthFirstTraversal(Long.parseLong(s)));
        }
        return stringSet;
    }
}
