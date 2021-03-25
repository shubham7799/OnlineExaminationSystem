package com.example.onlineexaminationsystem;

public class StudentRecord {
    private String stream,subject,examname;
    private int score;
    StudentRecord(){}
    StudentRecord(String stream,String subject,String examname,int score){
        this.stream=stream;
        this.subject=subject;
        this.examname=examname;
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public String getExamname() {
        return examname;
    }

    public String getStream() {
        return stream;
    }

    public String getSubject() {
        return subject;
    }
}
