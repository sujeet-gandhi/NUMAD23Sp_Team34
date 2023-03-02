package com.neu.numad23sp_team_34;

import java.util.Map;

public class FriendList {
    private Map<String, Boolean> friends;

    public FriendList() {}

    public FriendList(Map<String, Boolean> friends) {
        this.friends = friends;
    }

    public Map<String, Boolean> getFriends() {
        return friends;
    }
}

