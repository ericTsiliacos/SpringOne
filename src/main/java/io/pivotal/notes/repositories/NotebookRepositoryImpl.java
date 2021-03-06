package io.pivotal.notes.repositories;

import io.pivotal.notes.models.Note;
import io.pivotal.notes.models.Notebook;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class NotebookRepositoryImpl implements NotebookRepository {

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Notebook saveOrUpdateNotebook(Notebook notebook) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Notebook savedNotebook = em.merge(notebook);
        em.getTransaction().commit();
        return savedNotebook;
    }

    @Override
    public List<Notebook> getNotebooks() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("from Notebook", Notebook.class).getResultList();
    }

    @Override
    public Notebook getNotebookById(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Notebook.class, id);
    }

    @Override
    public Note saveOrUpdateNote(Note note) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Note savedNote = em.merge(note);
        em.getTransaction().commit();
        return savedNote;
    }

}
