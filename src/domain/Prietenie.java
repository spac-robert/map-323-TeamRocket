package domain;

import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private Long id1;
    private Long id2;

    public Prietenie(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "id prietenie=" + id +
                ", id1=" + id1 +
                ", id2=" + id2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(id1, prietenie.id1) && Objects.equals(id2, prietenie.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
