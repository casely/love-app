package com.example.savqa.love;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Fragment {

    private ArrayList<ParseUser> uList;

    public static ParseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_message, container, false);

        // TODO: Create chat
        //updateUserStatus(true);
        return rootView;
    }
/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserList();
    }

    private void updateUserStatus(boolean online) {
        user.put("online", online);
        user.saveEventually();
    }

    private void loadUserList() {
        final ProgressDialog dia = ProgressDialog.show(this, null, getString(R.string.alert_loading));
        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> li, ParseException e) {
                dia.dismiss();
                if (li != null) {
                    if (li.size() == 0)
                        Toast.makeText(MessageActivity.this, R.string.msg_no_user_found, Toast.LENGTH_SHORT).show();

                    uList = new ArrayList<ParseUser>(li);
                    ListView list = (ListView) getView().findViewById(R.id.list_item);
                    list.setAdapter(new User);
                }
            }
        })
    }*/

}