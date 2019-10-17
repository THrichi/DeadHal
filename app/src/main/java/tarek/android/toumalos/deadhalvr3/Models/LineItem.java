package tarek.android.toumalos.deadhalvr3.Models;

public class LineItem {
    private String goTo;
    private String uid;
    private int directionIcon;

    public LineItem() {
    }

    public LineItem(String goTo, String uid, int directionIcon) {
        this.goTo = goTo;
        this.directionIcon = directionIcon;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
