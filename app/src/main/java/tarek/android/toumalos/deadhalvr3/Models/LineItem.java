package tarek.android.toumalos.deadhalvr3.Models;

public class LineItem {
    private String goTo;
    private int directionIcon;

    public LineItem() {
    }

    public LineItem(String goTo, int directionIcon) {
        this.goTo = goTo;
        this.directionIcon = directionIcon;
    }

    public String getGoTo() {
        return goTo;
    }

    public void setGoTo(String goTo) {
        this.goTo = goTo;
    }

    public int getDirectionIcon() {
        return directionIcon;
    }

    public void setDirectionIcon(int directionIcon) {
        this.directionIcon = directionIcon;
    }
}
