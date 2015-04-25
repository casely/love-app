package com.example.savqa.love;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

public class MainActivity extends ActionBarActivity {

    private Button logoutButton;

    private String email = VKSdk.getAccessToken().email;
    private String userid = VKSdk.getAccessToken().userId;

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mClient = new MobileServiceClient(
                    "https://yktloveapp.azure-mobile.net/",
                    "lTbKOyKilxMTEDhLUZCCiXAnGpknYy10",
                    this
            );

        VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {

        @Override
        public void onComplete (VKResponse response) {
                VKApiUser user = ((VKList<VKApiUser>)response.parsedModel).get(0);
                TextView t = (TextView)findViewById(R.id.editText);
                t.setText("Привет " + user.first_name + " " + user.last_name);
            }
        });

            UserInfo item = new UserInfo();

            item.Text = "privet";
            item.LastName = "";

            mClient.getTable(UserInfo.class).insert(item, new TableOperationCallback<UserInfo>() {
                public void onCompleted(UserInfo entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        Log.v("My Project", "InsertSuccess");
                    } else {
                       Log.d("My Project", "InsertFail");
                    }
                }
            });

        } catch (MalformedURLException e) {
            new Exception("There was an error creating the Mobile Service. Verify the URL");
        }


        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKSdk.logout();
                startLoginActivity();
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

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
