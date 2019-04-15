package hu.unideb.smartcampus.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.unideb.smartcampus.R;
import hu.unideb.smartcampus.activity.main.MainActivity;
import hu.unideb.smartcampus.dialog.loading.LoadingDialog;
import hu.unideb.smartcampus.pojo.login.ActualUserInfo;

import static hu.unideb.smartcampus.container.Container.MY_REQUEST_CODE;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginActivity extends AppCompatActivity implements LoginTask.AsyncResponse {

    LoadingDialog loadingDialog;

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

    @OnClick(R.id.login_button)
    public void loginAction() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_REQUEST_CODE);
            return;
        }
        login();
    }

    private void login() {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.usernamePasswordNeed, Toast.LENGTH_SHORT).show();
        } else {
            loadingDialog = new LoadingDialog();
            loadingDialog.show(getFragmentManager(), "LOADING");
            LoginTask loginTask = new LoginTask(this);
            ActualUserInfo loginInput = new ActualUserInfo(username.getText().toString(), password.getText().toString(), null);
            loginTask.execute(loginInput);
        }
    }

    public void processFinish(int httpResponseCode) {
        loadingDialog.dismiss();
        if (httpResponseCode == HTTP_OK) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            System.err.println("error " + httpResponseCode);
            //TODO show error
        }
    }


}
