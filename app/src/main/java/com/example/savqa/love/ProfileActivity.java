package com.example.savqa.love;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProfileActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        outputInformation();

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

    private void outputInformation() {

           final VKRequest request = VKApi.users().get
                    (VKParameters.from(VKApiConst.FIELDS, "first_name, photo_200, bdate"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    VKApiUserFull user = ((VKList<VKApiUserFull>) response.parsedModel).get(0);
                    new DownloadImageTask((ImageView) getView().findViewById(R.id.ava))
                            .execute(user.photo_200);

                    TextView t = (TextView) getView().findViewById(R.id.editText);

                    String yearBirth = user.bdate;
                    String birth[] = yearBirth.split("\\.");

                    int dbirth = Integer.parseInt(birth[0]);
                    int mbirth = Integer.parseInt(birth[1]);
                    int ybirth = Integer.parseInt(birth[2]);

                    int age = getAge(dbirth, mbirth, ybirth);

                    t.setText(user.first_name + ", " + age);
                }
            });
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
