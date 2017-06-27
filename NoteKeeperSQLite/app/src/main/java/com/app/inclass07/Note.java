package com.app.inclass07;

public class Note {
    private long id;
    private String text, priority, status, update_time;


    public Note(String text, String priority, String status, String update_time) {
        this.text = text;
        this.priority = priority;
        this.status = status;
        this.update_time = update_time;
    }

    public Note(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
