package ru.mmekhtiev.petrolconsumption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private final static String[] refuelingNames = { "01.03.2016", "23.03.2016", "1.04.2016", "12.04.2016"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //found the list
        ListView petrolList = (ListView) findViewById(R.id.refuelingList);

        //create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.refueling_list_item, R.id.data, refuelingNames);

        //add adapter to list
        petrolList.setAdapter(adapter);
    }
}
