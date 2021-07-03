package com.app.e_notice;

import java.io.Serializable;

public class Comment implements Serializable {

    int id;
    String group_id, student_id, notice_id, text, created_at;

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", group_id='" + group_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", notice_id='" + notice_id + '\'' +
                ", text='" + text + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
