package com.example.gymplanner;

import android.os.Parcel;
import android.os.Parcelable;

public class Plan implements Parcelable {
    private Training training;
    private int minutes;
    private String day;
    private Boolean isAccomplished;

    public Plan(Training training, String day, int minutes, Boolean isAccomplished) {
        this.training = training;
        this.day = day;
        this.minutes = minutes;
        this.isAccomplished = isAccomplished;
    }

    protected Plan(Parcel in) {
        training = in.readParcelable(Training.class.getClassLoader());
        day = in.readString();
        minutes = in.readInt();
        byte tmpIsAccomplished = in.readByte();
        isAccomplished = tmpIsAccomplished == 0 ? null : tmpIsAccomplished == 1;
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

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public String getDay() {
        return day;
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

    public Boolean isAccomplished() {
        return isAccomplished;
    }

    public void setAccomplished(Boolean accomplished) {
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
        dest.writeByte((byte) (isAccomplished == null ? 0 : isAccomplished ? 1 : 2));
    }
}
