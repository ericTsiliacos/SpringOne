package io.pivotal.notes.repositories;

import io.pivotal.notes.models.Notebook;

public interface NotebookRepository {

    Notebook saveNotebook(Notebook notebook);

}
