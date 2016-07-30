package io.pivotal.notes.repositories;

import io.pivotal.notes.NotesApplication;
import io.pivotal.notes.models.Note;
import io.pivotal.notes.models.Notebook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotesApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotebookRepositoryImplTest {

    private NotebookRepository notebookRepository;

    @Autowired
    public void setNotebookRepository(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @Test
    public void should_saveNewNotebook() {
        Notebook notebook = notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title"));

        assertThat(notebook.getId()).isEqualTo(1);
        assertThat(notebook.getTitle()).isEqualTo("Title");
    }

    @Test
    public void should_updateNotebook() {
        Notebook notebook = notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title"));
        notebook.setTitle("New Title");

        notebook = notebookRepository.saveOrUpdateNotebook(notebook);
        assertThat(notebook.getId()).isEqualTo(1);
        assertThat(notebook.getTitle()).isEqualTo("New Title");
    }

    @Test
    public void should_getNotebooks() {
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title 1"));
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title 2"));

        List<Notebook> notebooks = notebookRepository.getNotebooks();
        assertThat(notebooks.get(0).getId()).isEqualTo(1);
        assertThat(notebooks.get(0).getTitle()).isEqualTo("Title 1");
        assertThat(notebooks.get(1).getId()).isEqualTo(2);
        assertThat(notebooks.get(1).getTitle()).isEqualTo("Title 2");
    }

    @Test
    public void should_getNotebookById() {
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title 1"));
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Title 2"));

        Notebook notebook = notebookRepository.getNotebookById(2);
        assertThat(notebook.getId()).isEqualTo(2);
        assertThat(notebook.getTitle()).isEqualTo("Title 2");
    }

    @Test
    public void should_saveNewNote() {
        Note note = notebookRepository.saveOrUpdateNote(new Note(null, "Hello, world!"));

        assertThat(note.getId()).isEqualTo(1);
        assertThat(note.getBody()).isEqualTo("Hello, world!");
    }

    @Test
    public void should_updateNote() {
        Note note = notebookRepository.saveOrUpdateNote(new Note(null, "Hello, world!"));
        note.setBody("Hello, sun!");

        note = notebookRepository.saveOrUpdateNote(note);
        assertThat(note.getId()).isEqualTo(1);
        assertThat(note.getBody()).isEqualTo("Hello, sun!");
    }
}