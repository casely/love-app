package com.example.savqa.love;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import com.parse.ParseUser;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

public class MainActivity extends Activity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OutputWelcomeTextVK();
    }


    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void OutputWelcomeTextVK() {
        VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete (VKResponse response) {
                VKApiUser user = ((VKList<VKApiUser>)response.parsedModel).get(0);
                TextView t = (TextView)findViewById(R.id.editText);
                t.setText("Привет " + user.first_name);


            }
        });
    }

    /*private void OutputWelcomeTextParse() {
        String a = ParseUser.getCurrentUser().getUsername();

        TextView t = (TextView)findViewById(R.id.editText);
        t.setText("Привет " + a);
    }*/


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
        if (id == R.id.logout) {
            VKSdk.logout();
            ParseUser.logOut();
            startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }
}
