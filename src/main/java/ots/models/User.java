package ots.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final String name;
    private final String email;
    private final String password;
    private final UserType type;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("type") String type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = UserType.valueOf(type.toUpperCase());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }
}
