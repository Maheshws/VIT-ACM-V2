package com.mahesh.vitacm;

/**
 * Created by Mahesh on 12/28/13.
 */
public class RegEventsObject {

    private int RegID;
    private String EventTitle;

    public RegEventsObject(int regID, String eventTitle) {
        RegID = regID;
        EventTitle = eventTitle;
    }


    public int getRegID() {
        return RegID;
    }


    public String getEventTitle() {
        return EventTitle;
    }
}
