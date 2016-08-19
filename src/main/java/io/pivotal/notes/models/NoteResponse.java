package io.pivotal.notes.models;

public class NoteResponse extends BaseResponse {

    private Note note;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
