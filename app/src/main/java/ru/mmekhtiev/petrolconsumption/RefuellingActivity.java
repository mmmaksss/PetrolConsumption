package ru.mmekhtiev.petrolconsumption;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RefuellingActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }

    @Override
    protected Fragment createFragment() {
        return new RefuellingFragment();
    }
}
