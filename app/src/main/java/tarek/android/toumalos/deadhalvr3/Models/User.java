package tarek.android.toumalos.deadhalvr3.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private List<String> viewMazes;
    private List<String> editableMazes;
    private List<String> mazes;

    public User() {
        this.viewMazes = new ArrayList<>();
        this.editableMazes = new ArrayList<>();
        this.mazes = new ArrayList<>();
    }

    public User(String uid, String name) {
        this.name = name;
        this.uid = uid;
        this.viewMazes = new ArrayList<>();
        this.editableMazes = new ArrayList<>();
        this.mazes = new ArrayList<>();
    }

    public User(String uid, String name, List<String> viewMazes, List<String> editableMazes, List<String> mazes) {
        this.name = name;
        this.uid = uid;
        this.viewMazes = viewMazes;
        this.editableMazes = editableMazes;
        this.mazes = mazes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getViewMazes() {
        return viewMazes;
    }

    public void setViewMazes(List<String> viewMazes) {
        this.viewMazes = viewMazes;
    }

    public List<String> getEditableMazes() {
        return editableMazes;
    }

    public void setEditableMazes(List<String> editableMazes) {
        this.editableMazes = editableMazes;
    }

    public List<String> getMazes() {
        return mazes;
    }

    public void setMazes(List<String> mazes) {
        this.mazes = mazes;
    }
}
