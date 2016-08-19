package io.pivotal.notes.models;

public class UserResponse extends BaseResponse {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
