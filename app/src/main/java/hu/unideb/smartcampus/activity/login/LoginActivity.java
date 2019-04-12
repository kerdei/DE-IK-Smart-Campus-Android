package hu.unideb.smartcampus.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.unideb.smartcampus.R;
import hu.unideb.smartcampus.activity.main.MainActivity;
import hu.unideb.smartcampus.dialog.loading.LoadingDialog;
import hu.unideb.smartcampus.pojo.login.ActualUserInfo;
import hu.unideb.smartcampus.task.login.BasicAuth;
import hu.unideb.smartcampus.task.login.LoginTask;
import hu.unideb.smartcampus.task.pojo.ReturnPojo;
import hu.unideb.smartcampus.xmpp.Connection;

import static hu.unideb.smartcampus.container.Container.MY_REQUEST_CODE;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_username_edittext)
    EditText username;

    @BindView(R.id.login_password_edittext)
    EditText password;

    @BindView(R.id.login_username_error_image_view)
    ImageView usernameError;

    @BindView(R.id.login_password_error_image_view)
    ImageView passwordError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    private void login() {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.usernamePasswordNeed, Toast.LENGTH_SHORT).show();
        } else {
            new LoginTask(this).execute(new ActualUserInfo(username.getText().toString(), password.getText().toString(), null));
        }
    }


    @OnClick(R.id.login_button)
    public void loginAction() {
        //TESZT
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_REQUEST_CODE);
            return;
        }

        login();
    }
}
