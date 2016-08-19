package io.pivotal.notes.models;

public class NotebookResponse extends BaseResponse {

    private Notebook notebook;

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }
}
