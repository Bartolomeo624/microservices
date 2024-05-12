package com.pioon.auth.user;

import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    User getUser(long id);
    Optional<User> getUserByUsername(String username);
}
