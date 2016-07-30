package io.pivotal.notes.repositories;

import io.pivotal.notes.models.Notebook;

import java.util.List;

public interface NotebookRepository {

    Notebook saveOrUpdateNotebook(Notebook notebook);
    List<Notebook> getNotebooks();

}
