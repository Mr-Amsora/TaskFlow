package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.User;
import com.ammar.taskflow.exception.DuplicateUserException;
import com.ammar.taskflow.exception.InvalidUserEmailException;
import com.ammar.taskflow.repository.UserRepository;

import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email){
        validateEmail(email);
        if (userRepository.existsByEmail(email)){
            throw new DuplicateUserException("The email is used");
        }
        return userRepository.save(new User(name, email));
    }

    public User updateUserEmail(Long id, String email){
        validateEmail(email);
        if (userRepository.existsByEmail(email)){
            throw new DuplicateUserException("The email is used");
        }
        User user = userRepository.findById(id);
        if (user == null){
            throw new InvalidUserEmailException("there is no user with this id");
        }
        user.updateEmail(email);
        return userRepository.update(user);
    }

    public User updateUserName(Long id, String name){
        User user = userRepository.findById(id);
        if (user == null){
            throw new InvalidUserEmailException("there is no user with this id");
        }
        user.updateUserName(name);
        return userRepository.update(user);
    }

    public void deleteUserById(Long id){
        if (userRepository.findById(id) == null){
            throw new InvalidUserEmailException("there is no user with this id");
        }
        userRepository.deleteById(id);
    }

    public User getUserById(Long id){
        User user = userRepository.findById(id);
        if (user == null){
            throw new InvalidUserEmailException("there is no user with this id");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserEmailException("Invalid email format");
        }
    }
}
