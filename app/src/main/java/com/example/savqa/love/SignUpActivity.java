package com.example.savqa.love;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;

import java.util.Calendar;

public class SignUpActivity extends Activity {

    private EditText nameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private EditText emailView;
    public RadioGroup radioGenderGroup;


    public EditText dpDate;
    private int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void ShowDialog() {
        dpDate = (EditText)findViewById(R.id.dp);
        dpDate.setInputType(InputType.TYPE_NULL);
        dpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDialog(DIALOG_ID);
            }
        });

        dpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(DIALOG_ID);
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerlistener, year_x, month_x,day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerlistener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    dpDate.setText(day_x + "." + month_x + "." + year_x);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ShowDialog();
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        nameView = (EditText) findViewById(R.id.name_edit_text);
        passwordView = (EditText) findViewById(R.id.password_edit_text);
        passwordAgainView = (EditText) findViewById(R.id.password_again_edit_text);
        emailView = (EditText)findViewById(R.id.email_edit_text);

        passwordAgainView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    SignUp();
                    return true;
                }
                return false;
            }
        });

        // Кнопка регистрации
        Button mActionButton = (Button) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        String name = nameView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String passwordAgain = passwordAgainView.getText().toString().trim();
        String email = emailView.getText().toString().trim();
        String date = dpDate.getText().toString().trim();

        // Проверка полей
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (isValidEmail(email) == false) {
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
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // Выдача сообщения об ошибке
        if (validationError) {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Прогресс диалог
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // Создание нового юзера в таблице
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.put("firstname", name);
        user.setPassword(password);
        user.setEmail(email);
        user.put("dateofbirth", date);

        radioGenderGroup = (RadioGroup)findViewById(R.id.radioSex);
        switch (radioGenderGroup.getCheckedRadioButtonId()) {
            case R.id.radioMale:
                user.put("gender", 2);
                break;
            case R.id.radioFemale:
                user.put("gender", 1);
                break;
            default:
                Toast.makeText(SignUpActivity.this, "Выберите пол", Toast.LENGTH_LONG).show();
                break;

        }


        // Сохранение данных в таблицу
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Выдача сообщения
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Октрытие следующего интента при успехе
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }



}
