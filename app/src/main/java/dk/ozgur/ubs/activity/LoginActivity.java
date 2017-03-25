package dk.ozgur.ubs.activity;


import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import dk.ozgur.ubs.Api;
import dk.ozgur.ubs.R;
import dk.ozgur.ubs.User;
import dk.ozgur.ubs.model.UserLoginRequest;
import dk.ozgur.ubs.model.UserLoginResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class LoginActivity extends BaseActivity {

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mLoginButton;

    private Call<UserLoginResponse> loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUsername = (EditText) findViewById(R.id.EditTextUsername);
        mEditTextPassword = (EditText) findViewById(R.id.EditTextPassword);

        mLoginButton = (Button) findViewById(R.id.ButtonLogin);
        mLoginButton.setText("Giriş");
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if(TextUtils.isEmpty(username)) {
            return;
        }

        if(TextUtils.isEmpty(password)) {
            return;
        }

        mLoginButton.setEnabled(false);
        mLoginButton.setText("...");

        loginRequest = Api.service.getLogin(new UserLoginRequest(username, password));
        loginRequest.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Response<UserLoginResponse> response) {

                mLoginButton.setEnabled(true);
                mLoginButton.setText("Giriş");

                Log.d("LoginAct", "response: " + response.message());

                if(response.isSuccess()) {
                    onLoginResponse(response.body());
                } else {
                    toast(R.string.error);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                toast(t.getMessage());
                mLoginButton.setEnabled(true);
                mLoginButton.setText("Giriş");
            }
        });
    }

    private void onLoginResponse(UserLoginResponse userLoginResponse) {

        if(userLoginResponse.hasError()) {
            toast(userLoginResponse.getMessage());
            return;
        }

        User user = new User(this);
        UserLoginResponse.UserLoginResponseStudent student = userLoginResponse.getUser().getStudent();
        user.setStudentName(student.getName() + " " + student.getSurname())
            .setGroupId(student.getUsersGroupId())
            .setStudentNo(student.getStudentNo())
            .setPassword(mEditTextPassword.getText().toString())
            .setStudentId(student.getStudentId())
            .setImage(student.getImage());

        // switch to main
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(loginRequest != null) {
            loginRequest.cancel();
        }
    }
}

