package ru.mmekhtiev.petrolconsumption;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class RefuellingActivity extends SingleFragmentActivity {

    public static final String EXTRA_REF_ID = "ru.mmekhtiev.petrolconsumption.ref_id";

    public static Intent newIntent(Context packageContext, UUID refId) {
        Intent intent = new Intent(packageContext, RefuellingActivity.class);
        intent.putExtra(EXTRA_REF_ID, refId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }

    @Override
    protected Fragment createFragment() {
        UUID refuelId = (UUID) getIntent().getSerializableExtra(EXTRA_REF_ID);
        return RefuellingFragment.newInstance(refuelId);
    }
}
