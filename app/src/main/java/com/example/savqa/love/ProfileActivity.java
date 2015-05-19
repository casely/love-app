package com.example.savqa.love;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProfileActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        String firstName = ParseUser.getCurrentUser().getString("firstname");
        String dateOfBirth = ParseUser.getCurrentUser().getString("dateofbirth");

        String birth[] = dateOfBirth.split("\\.");

        int dbirth = Integer.parseInt(birth[0]);
        int mbirth = Integer.parseInt(birth[1]);
        int ybirth = Integer.parseInt(birth[2]);

        int age = getAge(dbirth, mbirth, ybirth);

        TextView t = (TextView) rootView.findViewById(R.id.editText);
       t.setText(firstName + ", " + age);

        // Кнопка настроек
        Button mActionButton = (Button) rootView.findViewById(R.id.set_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public int getAge(int day, int month, int year) {

        GregorianCalendar cal = new GregorianCalendar();

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);

        int age = y - year;
        if (m < month) {
            age--;
        } else if (m == month && d < day) {
            age--;
        }
        return age;
    }
}
