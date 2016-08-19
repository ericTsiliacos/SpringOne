package io.pivotal.notes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.notes.NotesApplication;
import io.pivotal.notes.models.Note;
import io.pivotal.notes.models.Notebook;
import io.pivotal.notes.repositories.NotebookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotesApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotebookControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        NotebookController controller = new NotebookController(notebookRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void should_return200Ok_andSavedNotebook_when_newNotebookSaved() throws Exception {
        String body = objectMapper.writeValueAsString(new Notebook(null, "Title"));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void should_return400BadRequest_when_notebookTitleIsNullOrEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(new Notebook(null, null));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        body = objectMapper.writeValueAsString(new Notebook(null, ""));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return200Ok_andNotebooks_when_gettingAllNotebooks() throws Exception {
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Notebook 1"));
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Notebook 2"));
        mockMvc.perform(get("/api/v1/notebooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Notebook 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Notebook 2"));
    }

    @Test
    public void should_return200Ok_andNotebook_when_gettingNotebookById() throws Exception {
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Notebook 1"));
        notebookRepository.saveOrUpdateNotebook(new Notebook(null, "Notebook 2"));
        mockMvc.perform(get("/api/v1/notebook?id=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Notebook 2"));
    }

    @Test
    public void should_return200Ok_andSavedNote_when_newNoteSaved() throws Exception {
        String body = objectMapper.writeValueAsString(new Note(null, "Hello, world!"));
        mockMvc.perform(post("/api/v1/note").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.body").value("Hello, world!"));
    }

    @Test
    public void should_return200Ok_andUpdatedNote_when_noteUpdated() throws Exception {
        String body = objectMapper.writeValueAsString(new Note(null, "Hello, world!"));
        mockMvc.perform(post("/api/v1/note").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        body = objectMapper.writeValueAsString(new Note(1, "Hello, sun!"));
        mockMvc.perform(post("/api/v1/note").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.body").value("Hello, sun!"));
    }

    @Test
    public void should_return400BadRequest_when_notebookBodyIsNullOrEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(new Notebook(null, null));
        mockMvc.perform(post("/api/v1/note").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        body = objectMapper.writeValueAsString(new Notebook(null, ""));
        mockMvc.perform(post("/api/v1/note").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}