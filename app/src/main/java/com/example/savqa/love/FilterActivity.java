package com.example.savqa.love;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.ParseUser;

public class FilterActivity extends FragmentActivity {

    public RadioGroup radioGenderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String firstName = ParseUser.getCurrentUser().getString("firstname");

        TextView t = (TextView) findViewById(R.id.whoFind);
        t.setText("Кого ищем, " + firstName + "?");

        radioGenderGroup = (RadioGroup)findViewById(R.id.radioFilterSex);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
