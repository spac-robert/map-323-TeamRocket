package domain;

public enum StatusFriendRequest {
    PENDING("pending"), REJECT("reject"), APPROVAL("approval");
    private final String request;

    StatusFriendRequest(String request) {
        this.request = request;
    }

    public static StatusFriendRequest toString(String request) {
        for (StatusFriendRequest status : StatusFriendRequest.values()) {
            if (status.request.equals(request)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status");
    }

    public String convertToString() {
        return request;
    }
}
