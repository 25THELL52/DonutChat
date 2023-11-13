package com.example.donutchat.model;

public class Group {

    long groupID;
    String groupName;

    public Group(long groupID, String groupName)
    {
        this.groupID= groupID;
        this.groupName = groupName;
    }

    public long getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }
}
