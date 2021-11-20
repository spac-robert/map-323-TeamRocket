package repository.graph;

import domain.User;
import repository.file.AbstractFileRepository;

import java.util.*;

public interface Graph<ID> {

    boolean addVertex(ID id);

    List<ID> getAdjVertices(ID id);

    void addEdge(ID id1, ID id2);

    Set<ID> breadthFirstTraversal(ID id);

    Set<Set<ID>> poolOfConnectedComponents();

    int getNrOfConnectedComponents();

    Set<ID> getLargestConnectedComponent();
}
