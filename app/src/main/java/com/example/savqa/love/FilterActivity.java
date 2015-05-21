package com.example.savqa.love;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.savqa.love.util.RangeSeekBar;
import android.widget.LinearLayout;

import com.parse.ParseUser;

public class FilterActivity extends FragmentActivity {

    public RadioGroup radioGenderGroup;

    int sex;
    int mini;
    int maxi;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String firstName = ParseUser.getCurrentUser().getString("firstname");

        TextView t = (TextView) findViewById(R.id.whoFind);
        t.setText("Кого ищем, " + firstName + "?");

        age = ParseUser.getCurrentUser().getInt("age");

        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(this);
        rangeSeekBar.setRangeValues(15, 90);

        radioGenderGroup = (RadioGroup)findViewById(R.id.radioFilterSex);
        if (ParseUser.getCurrentUser().getInt("gender") == 1) {
            radioGenderGroup.check(R.id.radioMan);
            mini = age - 2;
            maxi = age + 10;
            rangeSeekBar.setSelectedMinValue(mini);
            rangeSeekBar.setSelectedMaxValue(maxi);
        }
        else if (ParseUser.getCurrentUser().getInt("gender") == 2) {
            radioGenderGroup.check(R.id.radioWoman);
            mini = 18;
            maxi = age + 2;
            rangeSeekBar.setSelectedMinValue(mini);
            rangeSeekBar.setSelectedMaxValue(maxi);
        }

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        Button mActionButton = (Button) findViewById(R.id.filterButton);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                switch (radioGenderGroup.getCheckedRadioButtonId()) {
                    case R.id.radioMan:
                        sex = 2;
                        break;
                    case R.id.radioWoman:
                        sex = 1;
                        break;
                    default:
                        break;
                }

                mini = rangeSeekBar.getSelectedMinValue();
                maxi = rangeSeekBar.getSelectedMaxValue();

                Intent i = new Intent(FilterActivity.this, MainActivity.class);
                i.putExtra("sex", sex);
                i.putExtra("min", mini);
                i.putExtra("max", maxi);
                startActivity(i);
            }
        });
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
