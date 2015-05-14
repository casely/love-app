package com.example.savqa.love;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

import java.io.InputStream;


public class LoginActivity extends Activity {

    private static final String appId = "4890105";
    public static final String TOKEN_KEY = "VK_ACCESS_TOKEN";

    // Права доступа
    public static final String[] scope = new String[] {
            VKScope.NOHTTPS,
            VKScope.PHOTOS
    };


    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VKUIHelper.onCreate(this);

        VKSdk.initialize(sdkListener, appId, VKAccessToken.tokenFromSharedPreferences(this, TOKEN_KEY));


        // Проверка был ли вход
        if (VKSdk.wakeUpSession() || ParseUser.getCurrentUser() != null) {
            startMainActivity();
        }

        // Обработчик нажатия на кнопку логина через ВК
        findViewById(R.id.loginBtnVK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   VKSdk.authorize(scope, true, false);
            }
        });

        // Опечаток ВК sdk
        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("Fingerprint", fingerprint[0]);

        emailEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    UserLogin();
                    return true;
                }
                return false;
            }
        });

        // Кнопка логина
        Button actionButton = (Button) findViewById(R.id.loginBtn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserLogin();
            }
        });

        // Кнопка регистрации
        findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }


    // Переопредение методов ВК
    private final VKSdkListener sdkListener = new VKSdkListener() {
        // Выдача капчи
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        // При ошибке токена
        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(scope);
        }

        // При закрытии доступа
        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.errorMessage)
                    .show();
        }

        // При получении нового токена
        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            startMainActivity();
        }

        // При успешной проверке токена
        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            startMainActivity();
        }

    };

    // Открытие mainactivity
    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }


    private void SignUpFromVK() {
        final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name, bdate, sex"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiUserFull user = ((VKList<VKApiUserFull>)response.parsedModel).get(0);

                ParseUser puser = new ParseUser();
                puser.setUsername("vk" + user.id);
                puser.put("firstname", user.first_name);
                puser.setPassword("111");
                puser.put("dateofbirth", user.bdate);
                puser.put("gender", user.sex);

                puser.signUpInBackground();


            }
        });

    }

    // Переопределение методов активити для ВК Апи
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        SignUpFromVK();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        VKUIHelper.onActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {

    }

    // Метод логина
    private void UserLogin() {

        // Получение значений с текстовых полей
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Валидация (проверка ввода)
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (email.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // Выдача тоста при ошибке
        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Создание прогрессдиалога
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.progress_login));
        dialog.show();

        // Проверка юзернэйма и пароля на совпадение со значениями в таблице
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Показ тоста ошибки
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Открытие интента при успехе
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
