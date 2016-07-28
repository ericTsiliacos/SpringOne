package io.pivotal.notes.repositories;

import io.pivotal.notes.NotesApplication;
import io.pivotal.notes.models.Notebook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}