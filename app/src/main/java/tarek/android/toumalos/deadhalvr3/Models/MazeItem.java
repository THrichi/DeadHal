package tarek.android.toumalos.deadhalvr3.Models;


public class MazeItem {
    private String name;
    private int open;
    private int details;
    private int delete;

    public MazeItem() {

    }

    public MazeItem(String name, int open, int details, int delete) {
        this.name = name;
        this.open = open;
        this.details = details;
        this.delete = delete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getDetails() {
        return details;
    }

    public void setDetails(int details) {
        this.details = details;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
