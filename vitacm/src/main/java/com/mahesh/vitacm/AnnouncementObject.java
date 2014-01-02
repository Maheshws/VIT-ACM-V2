package com.mahesh.vitacm;

/**
 * Created by Mahesh on 1/2/14.
 */
public class AnnouncementObject {
    private int AID;
    private String ATitle;
    private String AContent;

    public AnnouncementObject(int AID, String ATitle, String AContent) {
        this.AID = AID;
        this.ATitle = ATitle;
        this.AContent = AContent;
    }

    public int getAID() {
        return AID;
    }

    public String getATitle() {
        return ATitle;
    }

    public String getAContent() {
        return AContent;
    }
}
