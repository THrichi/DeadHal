package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Point;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private int choice;
    private int oldColor;
    private String name;

    public OptionItem(String name, int oldColor,int choice) {
        this.choice = choice;
        this.oldColor = oldColor;
        this.name = name;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getOldColor() {
        return oldColor;
    }

    public void setOldColor(int oldColor) {
        this.oldColor = oldColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
