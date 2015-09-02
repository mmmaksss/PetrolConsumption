package ru.mmekhtiev.petrolconsumption.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RefuelingListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(petrolconsumption.mmekhtiev.ru.petrolconsumption.R.layout.activity_refueling_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(petrolconsumption.mmekhtiev.ru.petrolconsumption.R.menu.menu_refueling_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == petrolconsumption.mmekhtiev.ru.petrolconsumption.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
