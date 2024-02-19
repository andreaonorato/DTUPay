package token.service;

import lombok.Getter;

public class User {
    @Getter
    private final String id;

    public User(String id) {
        this.id = id;
    }
}
