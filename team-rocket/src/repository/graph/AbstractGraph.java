package repository.graph;

import java.util.*;

public abstract class AbstractGraph<ID> implements Graph<ID> {
    protected Map<ID, List<ID>> adjVertices = new HashMap<>();

    @Override
    public void addVertex(ID id) {
        adjVertices.putIfAbsent(id, new ArrayList<>());
    }

    @Override
    public List<ID> getAdjVertices(ID id) {
        return adjVertices.get(id);
    }

    @Override
    public void addEdge(ID id1, ID id2) {
        adjVertices.get(id1).add(id2);
        adjVertices.get(id2).add(id1);
    }

    @Override
    public Set<ID> breadthFirstTraversal(ID id) {
        Set<ID> visited = new LinkedHashSet<>();
        Queue<ID> queue = new LinkedList<>();
        queue.add(id);
        visited.add(id);
        while (!queue.isEmpty()) {
            ID vertex = queue.poll();
            for (ID v : getAdjVertices(vertex)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
        return visited;
    }

    public int getNrOfConnectedComponents() {
        Set<Set<ID>> stringSet = poolOfConnectedComponents();
        return stringSet.size();
    }

    public Set<ID> getLargestConnectedComponent() {
        int max = 0;
        Set<ID> setMax = new HashSet<>();
        Set<Set<ID>> stringSet = poolOfConnectedComponents();
        for (Set<ID> set : stringSet) {
            if (set.size() > max) {
                max = set.size();
                setMax = set;
            }
        }
        return setMax;
    }
}
