package pro.marschall.uek.movealarm.models;

import java.io.Serializable;
import java.time.LocalTime;

public class AlarmModel implements Serializable {
    LocalTime time;
    boolean active;
    String dismissType;

    public AlarmModel(LocalTime time, boolean active, String dismissType) {
        this.time = time;
        this.active = active;
        this.dismissType = dismissType;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public String isActive() {
        return Boolean.toString(this.active);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDismissType() {
        return dismissType;
    }

    public void setDismissType(String dismissType) {
        this.dismissType = dismissType;
    }
}
