package Model;

public class Slot_Model {
     String morning_slot_start;
    String  morning_slot_end;
    String  noon_slot_start;
    String   noon_slot_end;
    String   night_slot_start;
    String   night_slot_end;

    public Slot_Model() {
    }

    public Slot_Model(String morning_slot_start, String morning_slot_end, String noon_slot_start, String noon_slot_end, String night_slot_start, String night_slot_end) {
        this.morning_slot_start = morning_slot_start;
        this.morning_slot_end = morning_slot_end;
        this.noon_slot_start = noon_slot_start;
        this.noon_slot_end = noon_slot_end;
        this.night_slot_start = night_slot_start;
        this.night_slot_end = night_slot_end;
    }

    public String getMorning_slot_start() {
        return morning_slot_start;
    }

    public void setMorning_slot_start(String morning_slot_start) {
        this.morning_slot_start = morning_slot_start;
    }

    public String getMorning_slot_end() {
        return morning_slot_end;
    }

    public void setMorning_slot_end(String morning_slot_end) {
        this.morning_slot_end = morning_slot_end;
    }

    public String getNoon_slot_start() {
        return noon_slot_start;
    }

    public void setNoon_slot_start(String noon_slot_start) {
        this.noon_slot_start = noon_slot_start;
    }

    public String getNoon_slot_end() {
        return noon_slot_end;
    }

    public void setNoon_slot_end(String noon_slot_end) {
        this.noon_slot_end = noon_slot_end;
    }

    public String getNight_slot_start() {
        return night_slot_start;
    }

    public void setNight_slot_start(String night_slot_start) {
        this.night_slot_start = night_slot_start;
    }

    public String getNight_slot_end() {
        return night_slot_end;
    }

    public void setNight_slot_end(String night_slot_end) {
        this.night_slot_end = night_slot_end;
    }
}
