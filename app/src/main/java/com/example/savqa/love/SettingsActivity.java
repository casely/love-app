package com.example.savqa.love;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;
import com.vk.sdk.VKSdk;


public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Кнопка регистрации
        Button mActionButton = (Button) findViewById(R.id.logout_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VKSdk.logout();
                ParseUser.logOut();
                startLoginActivity();
            }
        });
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
