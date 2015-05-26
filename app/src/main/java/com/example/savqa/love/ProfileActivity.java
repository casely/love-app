package com.example.savqa.love;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfileActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        String firstName = ParseUser.getCurrentUser().getString("firstname");
        int age = ParseUser.getCurrentUser().getInt("age");

        TextView t = (TextView) rootView.findViewById(R.id.editText);
        t.setText(firstName + ", " + age);

        return rootView;
    }


}
