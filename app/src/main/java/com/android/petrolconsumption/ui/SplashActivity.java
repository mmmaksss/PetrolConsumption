package com.android.petrolconsumption.ui;

import java.util.Date;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.PetrolConsumptionApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Splash activity
 */
public class SplashActivity extends Activity {

    private static final long SPLASH_MIN_DELAY_IN_MILISECONDS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // Starting asynchronous app initialization
        new ApplicationInitializationTask().execute();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    /**
     * Contains app's initialization logic
     */
    private class ApplicationInitializationTask extends
            AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Date beginDate = new Date();
            PetrolConsumptionApplication application = PetrolConsumptionApplication
                    .getInstance();
            if (application != null) {
                application.initializeApplication();
            }

            Date endDate = new Date();

            // Additional time for splash if initialization was done faster than
            // min splash delay time.
            long initTimeInMiliseconds = endDate.getTime()
                    - beginDate.getTime();
            if (initTimeInMiliseconds < SPLASH_MIN_DELAY_IN_MILISECONDS) {
                try {
                    Thread.sleep(SPLASH_MIN_DELAY_IN_MILISECONDS
                            - initTimeInMiliseconds);
                } catch (InterruptedException e) {
                    SplashActivity.this.finish();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(SplashActivity.this, VoiceActivity.class);
            startActivity(intent);
            finish();
            return;
        }

    }
}
