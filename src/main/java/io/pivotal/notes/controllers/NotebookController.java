package io.pivotal.notes.controllers;

import io.pivotal.notes.models.Error;
import io.pivotal.notes.models.*;
import io.pivotal.notes.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
class NotebookController {

    private final NotebookRepository notebookRepository;

    @Autowired
    NotebookController(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @RequestMapping(value = "notebook", method = RequestMethod.POST)
    public ResponseEntity<NotebookResponse> saveOrUpdateNotebook(@RequestBody Notebook notebook) {
        NotebookResponse response = new NotebookResponse();
        if (isValid(notebook)) {
            Notebook savedNotebook = notebookRepository.saveOrUpdateNotebook(notebook);
            response.setNotebook(savedNotebook);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setError(new Error("Title cannot be blank"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "notebooks", method = RequestMethod.GET)
    public ResponseEntity<List<Notebook>> getAllNotebooks() {
        List<Notebook> notebooks = notebookRepository.getNotebooks();
        return new ResponseEntity<>(notebooks, HttpStatus.OK);
    }

    @RequestMapping(value = "notebook/{id}", method = RequestMethod.GET)
    public ResponseEntity<NotebookResponse> getNotebookById(@PathVariable int id) {
        NotebookResponse response = new NotebookResponse();
        Notebook notebook = notebookRepository.getNotebookById(id);
        response.setNotebook(notebook);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "note", method = RequestMethod.POST)
    public ResponseEntity<NoteResponse> saveOrUpdateNote(@RequestBody Note note) {
        NoteResponse noteResponse = new NoteResponse();
        if (isValid(note)) {
            Note savedNote = notebookRepository.saveOrUpdateNote(note);
            noteResponse.setNote(savedNote);
            return new ResponseEntity<>(noteResponse, HttpStatus.OK);
        } else {
            noteResponse.setError(new Error("Body cannot be blank"));
            return new ResponseEntity<>(noteResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValid(Notebook notebook) {
        return !StringUtils.isEmpty(notebook.getTitle());
    }

    private boolean isValid(Note note) {
        return !StringUtils.isEmpty(note.getBody());
    }

}
