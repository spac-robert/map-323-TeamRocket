package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private User from;
    private List<User> to;
    private String msg;
    private LocalDateTime localDateTime;

    public Message(User from, List<User> to, String msg) {
        this.from = from;
        this.to = to;
        this.msg = msg;
        this.localDateTime = LocalDateTime.now();
    }

    public Message(User from, String msg) {
        this.from = from;
        this.msg = msg;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
