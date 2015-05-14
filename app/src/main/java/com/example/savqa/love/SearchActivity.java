package com.example.savqa.love;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class SearchActivity extends android.support.v4.app.ListFragment {

    String[] resultsAsString = {""};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("gender", 2);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    resultsAsString = new String[parseUsers.size()];
                    for (int i = 0; i < parseUsers.size(); i++) {
                        resultsAsString[i] = parseUsers.get(i).getString("firstname");
                        ArrayAdapter mArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsAsString);
                        setListAdapter(mArrayAdapter);
                    }
                } else {
                }
            }
        });
    }

}
