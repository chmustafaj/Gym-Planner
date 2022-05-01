package com.example.gymplanner;

import android.os.Parcel;
import android.os.Parcelable;
public class Plan implements Parcelable {
    private static int count=1;
    private int id =0;
    private Training training;
    private int minutes;
    private String day;
    private int isAccomplished;

    public Plan(Training training, String day, int minutes, int isAccomplished) {
        this.training = training;
        this.day = day;
        this.minutes = minutes;
        this.isAccomplished = isAccomplished;
        this.id = id++;
    }

    protected Plan(Parcel in) {
        training = in.readParcelable(Training.class.getClassLoader());
        day = in.readString();
        minutes = in.readInt();
        byte tmpIsAccomplished = in.readByte();
        isAccomplished = in.readInt();
        id=in.readInt();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

    public Plan() {
    }

    public Training getTraining() {
        return training;
    }
    public int getId(){return id;}
    public void setTraining(Training training) {
        this.training = training;
    }

    public String getDay() {
        return day;
    }
    public void setId(int id){
        this.id= id;
    }
    public void setDay(String day) {
        this.day = day;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int isAccomplished() {
        return isAccomplished;
    }

    public void setAccomplished(int accomplished) {
        isAccomplished = accomplished;
    }


    @Override
    public String toString() {
        return "Plan{" +
                "training=" + training +
                ", day='" + day + '\'' +
                ", minutes=" + minutes +
                ", isAccomplished=" + isAccomplished +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(training, flags);
        dest.writeString(day);
        dest.writeInt(minutes);
        dest.writeInt(isAccomplished);
    }
}