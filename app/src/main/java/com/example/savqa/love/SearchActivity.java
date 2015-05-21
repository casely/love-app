package com.example.savqa.love;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    int sex;
    int mini;
    int maxi;
    int age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Условия вывода пользователей по умолчанию
        // Если 1 - ж; 2 - м
        age = ParseUser.getCurrentUser().getInt("age");
        // Если женщина
        if (ParseUser.getCurrentUser().getInt("gender") == 1) {
            sex = 2;
            mini = age - 2;
            maxi = age + 10;
        }
        // Если мужчина
        else if (ParseUser.getCurrentUser().getInt("gender") == 2) {
            sex = 1;
            mini = 18;
            maxi = age + 2;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search, container, false);

        // Кнопка настроек
        Button mActionButton = (Button) rootView.findViewById(R.id.filter_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isNetworkConnected()) {
            new UserAsyncTask().execute();
        }
        else {
            TextView lv = (TextView) getActivity().findViewById(R.id.empty_el);
            lv.setText("Подключение к интернету отсутствует");
        }
    }

    private class UserAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Подождите, мы подбираем Вам пару");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                // Проверяем, есть ли записи с полем sex в extra интента
                if (getActivity().getIntent().getExtras() != null) {
                    Bundle args = getActivity().getIntent().getExtras();
                    sex = args.getInt("sex");
                    mini = args.getInt("min");
                    maxi = args.getInt("max");

                }
                // Формирование запроса к parse.com
                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("gender", sex);
                query.whereGreaterThanOrEqualTo("age", mini);
                query.whereLessThanOrEqualTo("age", maxi);

                query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (parseUsers != null) {
                        resultsAsString = new String[parseUsers.size()];
                            TextView lv = (TextView)getActivity().findViewById(R.id.empty_el);
                            // Вывод текста если нет пользователей
                            if (parseUsers.size() == 0)
                                lv.setText("Рядом с Вами нет новых людей");
                            // Вывод списка найденных пользователей
                            for (int i = 0; i < parseUsers.size(); i++) {
                                resultsAsString[i] = parseUsers.get(i).getString("firstname");
                                ListView lvMain = (ListView) getView().findViewById(R.id.lwMain);
                                ArrayAdapter mArrayAdapter = new ArrayAdapter(
                                    getActivity(), android.R.layout.simple_list_item_1, resultsAsString);
                                lvMain.setAdapter(mArrayAdapter);
                            }
                            // Закрытие диалога с баром
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
