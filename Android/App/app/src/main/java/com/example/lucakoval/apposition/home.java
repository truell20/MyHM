package com.example.lucakoval.apposition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //Intent i = getIntent();
        String email = getIntent().getStringExtra("email");
        System.out.println(email);
        //String password = i.getStringExtra("password");

        TextView home = (TextView) findViewById(R.id.homeText);
        home.setText(email);

        String[] textOptions={"Caf", "Library", "Class"};
        ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, textOptions);
        Spinner spinner = (Spinner) findViewById(R.id.options);
        spinner.setAdapter(stringArrayAdapter);

        final Spinner options = (Spinner) findViewById(R.id.options);
        final String choice = options.getSelectedItem().toString();

        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView locationText = (TextView) findViewById(R.id.locationText);

                locationText.setText(choice);
            }
        });

        Button schedule = (Button) findViewById(R.id.schedule);

        schedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), Schedule.class);
                startActivity(j);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
