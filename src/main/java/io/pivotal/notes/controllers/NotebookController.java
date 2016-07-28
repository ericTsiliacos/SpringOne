package io.pivotal.notes.controllers;

import io.pivotal.notes.models.Notebook;
import io.pivotal.notes.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NotebookController {

    private final NotebookRepository notebookRepository;

    @Autowired
    public NotebookController(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @RequestMapping(value = "notebook", method = RequestMethod.POST)
    public ResponseEntity<Notebook> saveNotebook(@RequestBody Notebook notebook) {
        if (isValid(notebook)) {
            Notebook savedNotebook = notebookRepository.saveNotebook(notebook);
            return new ResponseEntity<>(savedNotebook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValid(Notebook notebook) {
        return !StringUtils.isEmpty(notebook.getTitle());
    }

}
