package ru.mmekhtiev.petrolconsumption.data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mekhtievs on 17.09.2016.
 */
public class Refuelling {
    private UUID mId;
    private String title;
    private Date mDate;
    private boolean mFinished;

    public Refuelling() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }
}
