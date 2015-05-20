package com.example.savqa.love;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.ParseUser;

public class FilterActivity extends FragmentActivity {

    public RadioGroup radioGenderGroup;
    public EditText minVal;
    public EditText maxVal;

    int sex;
    int mini;
    int maxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String firstName = ParseUser.getCurrentUser().getString("firstname");

        TextView t = (TextView) findViewById(R.id.whoFind);
        t.setText("Кого ищем, " + firstName + "?");

        minVal = (EditText) findViewById(R.id.minValue);
        maxVal = (EditText) findViewById(R.id.maxValue);

        Button mActionButton = (Button) findViewById(R.id.filterButton);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                radioGenderGroup = (RadioGroup)findViewById(R.id.radioFilterSex);
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

                mini = Integer.parseInt(minVal.getText().toString());
                maxi = Integer.parseInt(maxVal.getText().toString());

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
