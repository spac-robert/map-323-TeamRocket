package domain;

public class FriendRequest extends Entity<Long> {
    private Entity<Long> from;
    private Entity<Long> to;
    private StatusFriendRequest status;

    public FriendRequest(Entity<Long> from, Entity<Long> to) {
        this.from = from;
        this.to = to;
        this.status = StatusFriendRequest.PENDING;
    }

    public Entity<Long> getFrom() {
        return from;
    }

    public void setFrom(Entity<Long> from) {
        this.from = from;
    }

    public Entity<Long> getTo() {
        return to;
    }

    public void setTo(Entity<Long> to) {
        this.to = to;
    }

    public String getStatus() {
        return status.convertToString();
    }

    public void setStatus(StatusFriendRequest status) {
        this.status = status;
    }
}
