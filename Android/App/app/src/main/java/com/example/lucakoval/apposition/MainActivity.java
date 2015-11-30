package com.example.lucakoval.apposition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import backend.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(getString(R.string.preferencesFileKey), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

        if(settings.getString(getString(R.string.emailKey), null) != null) {
            Intent i = new Intent(getApplicationContext(), TabActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.content_main);

        Button signInButton = (Button) findViewById(R.id.signInButton);

        // When button is clicked, save email and password and launch home tab
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailInput = (EditText) findViewById(R.id.emailInput);
                final String email = emailInput.getText().toString();

                EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
                final String password = passwordInput.getText().toString();

                Backend.getUserDataWithSignIn(email, password, new BackendCallback<UserData>() {
                    @Override
                    public void callback(UserData result) {
                        if(result == null) {
                            findViewById(R.id.errorMessage).setVisibility(View.VISIBLE);
                        } else {
                            // Store the email and password on the phone
                            SharedPreferences settings = getSharedPreferences(getString(R.string.preferencesFileKey), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(getString(R.string.emailKey), email);
                            editor.putString(getString(R.string.passwordKey), password);
                            editor.putInt(getString(R.string.userIDKey), result.userID);
                            editor.commit();

                            // Go to the TabActivity
                            Intent i = new Intent(getApplicationContext(), TabActivity.class);
                            startActivity(i);

                            // Kill the Sign In activity
                            finish();
                        }
                    }
                });
            }
        });
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
