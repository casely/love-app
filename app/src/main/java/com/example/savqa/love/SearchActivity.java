package com.example.savqa.love;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class SearchActivity extends Fragment {

    private ProgressDialog pDialog;
    String[] resultsAsString = {""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new UserAsyncTask().execute();
    }

    private class UserAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Поиск...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                int gender = ParseUser.getCurrentUser().getInt("gender");
                if (gender == 1)
                    query.whereEqualTo("gender", 2);
                else if (gender == 2)
                    query.whereEqualTo("gender", 1);
                query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {
                        if (parseUsers != null) {
                            resultsAsString = new String[parseUsers.size()];

                            TextView lv = (TextView)getActivity().findViewById(R.id.empty_el);

                            if (parseUsers.size() == 0)
                                lv.setText("Пользователи не найдены");

                                for (int i = 0; i < parseUsers.size(); i++) {
                                    resultsAsString[i] = parseUsers.get(i).getString("firstname");
                                    ListView lvMain = (ListView) getView().findViewById(R.id.lwMain);
                                    ArrayAdapter mArrayAdapter = new ArrayAdapter(
                                            getActivity(), android.R.layout.simple_list_item_1, resultsAsString);
                                    lvMain.setAdapter(mArrayAdapter);

                                }
                            pDialog.dismiss();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
