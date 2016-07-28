package io.pivotal.notes.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.notes.NotesApplication;
import io.pivotal.notes.models.Notebook;
import io.pivotal.notes.repositories.NotebookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotesApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotebookControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    public void setNotebookRepository(NotebookRepository notebookRepository) {
        NotebookController controller = new NotebookController(notebookRepository);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void should_return200Ok_andSavedNotebook_when_newNotebookSaved() throws Exception {
        String body = toHttpBody(new Notebook(null, "Title"));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void should_return400BadRequest_when_titleIsNullOrEmpty() throws Exception {
        String body = toHttpBody(new Notebook(null, null));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        body = toHttpBody(new Notebook(null, ""));
        mockMvc.perform(post("/api/v1/notebook").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String toHttpBody(Notebook notebook) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(notebook);
    }

}