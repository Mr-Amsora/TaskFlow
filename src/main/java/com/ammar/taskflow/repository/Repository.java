package com.ammar.taskflow.repository;

import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class Repository<T, ID> {

    public final EntityManager entityManager;
    public final Class<T> entityClass;

    public Repository(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }


    public T save(T entity){
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }

    public T findById(ID id){
       return entityManager.find(entityClass, id);
    }

    public T update(T entity){
        entityManager.getTransaction().begin();
        T mergedEntity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return mergedEntity;
    }

    public List<T> findAll() {
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(query, entityClass).getResultList();
    }

    public void deleteById(ID id){
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
    }
}
