package tarek.android.toumalos.deadhalvr3.Models;

import java.util.List;

public class User {
    private String name;
    private String id;
    private List<String> followingMazes;

    public User() {
    }

    public User(String name, String id, List<String> followingMazes) {
        this.name = name;
        this.id = id;
        this.followingMazes = followingMazes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFollowingMazes() {
        return followingMazes;
    }

    public void setFollowingMazes(List<String> followingMazes) {
        this.followingMazes = followingMazes;
    }
}
