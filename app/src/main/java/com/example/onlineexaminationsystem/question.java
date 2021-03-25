package com.example.onlineexaminationsystem;

import android.os.Parcel;
import android.os.Parcelable;

public class question implements Parcelable {
    private String quest,o1,o2,o3,o4;
    private int answer;
    question(){
        quest="";
        o1="";
        o2="";
        o3="";
        o4="";
        answer=0;
    }

    protected question(Parcel in) {
        quest = in.readString();
        o1 = in.readString();
        o2 = in.readString();
        o3 = in.readString();
        o4 = in.readString();
        answer = in.readInt();
    }

    public static final Creator<question> CREATOR = new Creator<question>() {
        @Override
        public question createFromParcel(Parcel in) {
            return new question(in);
        }

        @Override
        public question[] newArray(int size) {
            return new question[size];
        }
    };

    public int getAnswer() {
        return answer;
    }

    public String getO1() {
        return o1;
    }

    public String getO2() {
        return o2;
    }

    public String getO3() {
        return o3;
    }

    public String getO4() {
        return o4;
    }

    public String getQuest() {
        return quest;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setO1(String o1) {
        this.o1 = o1;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public void setO4(String o4) {
        this.o4 = o4;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quest);
        dest.writeString(o1);
        dest.writeString(o2);
        dest.writeString(o3);
        dest.writeString(o4);
        dest.writeInt(answer);
    }
}
