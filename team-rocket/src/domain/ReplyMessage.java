package domain;

public class ReplyMessage extends Entity<Long> {
    private Message message;
    private long idMsgToReply;

    public ReplyMessage(Message message, long idMsgToReply) {
        this.message = message;
        this.idMsgToReply = idMsgToReply;
    }

    public long getIdMsgToReply() {
        return idMsgToReply;
    }

    public void setIdMsgToReply(long idMsgToReply) {
        this.idMsgToReply = idMsgToReply;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
