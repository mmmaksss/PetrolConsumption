package ru.mmekhtiev.petrolconsumption.data;

import java.util.UUID;

/**
 * Created by Mekhtievs on 17.09.2016.
 */
public class Refuelling {
    private UUID mId;
    private String title;


    Refuelling() {
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
