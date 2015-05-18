package com.example.savqa.love;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Кнопка регистрации
        Button mActionButton = (Button) findViewById(R.id.logout_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.logOut();
                }
                startLoginActivity();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
