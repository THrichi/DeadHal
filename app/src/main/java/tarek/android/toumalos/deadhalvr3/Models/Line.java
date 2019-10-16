package tarek.android.toumalos.deadhalvr3.Models;

import java.io.Serializable;

public class Line implements Serializable {

    private String goToId;
    private String direction_first;
    private String direction_second;

    public Line() {
    }

    public Line(String goToId, String direction_first, String direction_second) {
        this.goToId = goToId;
        this.direction_first = direction_first;
        this.direction_second = direction_second;
    }

    public String getGoToId() {
        return goToId;
    }

    public void setGoToId(String goToId) {
        this.goToId = goToId;
    }

    public String getDirection_first() {
        return direction_first;
    }

    public void setDirection_first(String direction_first) {
        this.direction_first = direction_first;
    }

    public String getDirection_second() {
        return direction_second;
    }

    public void setDirection_second(String direction_second) {
        this.direction_second = direction_second;
    }
}
