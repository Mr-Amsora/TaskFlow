package com.ammar.taskflow.repository;

import com.ammar.taskflow.domain.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UserRepository extends Repository<User, Long> {
    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }

    public boolean existsByEmail(String email) {
        String query = "SELECT u FROM User u WHERE u.email = :email";
        List<User> results = entityManager.createQuery(query, User.class)
                .setParameter("email", email)
                .getResultList();
        return !results.isEmpty();
    }
}