package com.example.sep_r.notekeeper;

/**
 * Created by sep_r on 27-02-2017.
 */

public class Note {
    private String id;
    private String note, priority, update_time, status;

    public Note(String note, String priority, String update_time, String status) {
        this.note = note;
        this.priority = priority;
        this.update_time = update_time;
        this.status = status;
    }

    public Note()
    {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", priority='" + priority + '\'' +
                ", update_time='" + update_time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
