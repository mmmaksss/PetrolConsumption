package ru.mmekhtiev.petrolconsumption.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mekhtievs on 17.09.2016.
 */
public class RefuellingLab {
    private static RefuellingLab sRefuellingLab;

    private List<Refuelling> mRefuellings;
    private Context mAppContext;

    public static RefuellingLab get(Context context) {
        if (sRefuellingLab ==null) {
            sRefuellingLab = new RefuellingLab(context);
        }
        return sRefuellingLab;
    }

    private RefuellingLab(Context context) {
        mRefuellings = new ArrayList<>();
        mAppContext = context;
        for(int i=0; i<100; i++){
            Refuelling r = new Refuelling();
            r.setTitle("Refuelling # "+i);
            r.setFinished(i%2==0);
            mRefuellings.add(r);
        }
    }

    public List<Refuelling> getRefuellings(){
        return mRefuellings;
    }

    public Refuelling getRefuelling(UUID id) {
        for (Refuelling refuelling:mRefuellings){
            if(refuelling.getId().equals(id)) {
                return refuelling;
            }
        }
        return null;
    }
}

