package domain;

import java.util.*;

public class User extends Entity<Long> {
    private final String firstName;
    private final String lastName;
    private final List<User> friends = new ArrayList<>();

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        StringBuilder stringOfFriends = new StringBuilder("[");
        for (User user : friends) {
            String ut = "{" + user.getId() + "; " + user.firstName + "; " + user.lastName + "} ";
            stringOfFriends.append(ut);
        }
        return "User{" +
                "id = " + id + ", " +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + stringOfFriends + ']' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void makeFriend(User user) {
        this.friends.add(user);
    }
}