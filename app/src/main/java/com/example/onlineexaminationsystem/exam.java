package com.example.onlineexaminationsystem;

import java.util.ArrayList;

public class exam {
    private String deadlinestart,deadlineend,examname;
    private int numberquestion,time;
    ArrayList<question> arrayQuestion;
    exam(){}
    exam(int numberquestion,String deadlinestart,String deadlineend,int time,String examname,ArrayList<question> arrayList){
        this.numberquestion=numberquestion;
        this.deadlinestart=deadlinestart;
        this.deadlineend=deadlineend;
        this.examname=examname;
        this.time=time;
        arrayQuestion=arrayList;
    }

    public String getDeadlineend() {
        return deadlineend;
    }

    public String getDeadlinestart() {
        return deadlinestart;
    }

    public int getNumberquestion() {
        return numberquestion;
    }

    public String getExamname() {
        return examname;
    }

    public ArrayList<question> getArrayQuestion() {
        return arrayQuestion;
    }

    public int getTime() {
        return time;
    }
}
