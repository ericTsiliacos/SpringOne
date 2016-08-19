package io.pivotal.notes.repositories;

import io.pivotal.notes.crypto.EncryptionTool;
import io.pivotal.notes.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private EntityManagerFactory emf;
    private EncryptionTool encryptionTool;

    @Autowired
    UserRepositoryImpl(EncryptionTool encryptionTool) {
        this.encryptionTool = encryptionTool;
    }

    @PersistenceUnit
    void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User saveOrUpdateUser(Integer id, String username, String password) {
        String encryptedPassword = encryptionTool.encrypt(password);
        User user = new User(id, username, encryptedPassword);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User savedUser = em.merge(user);
        em.getTransaction().commit();
        return savedUser;
    }

    @Override
    public User getUserById(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(User.class, id);
    }
}
