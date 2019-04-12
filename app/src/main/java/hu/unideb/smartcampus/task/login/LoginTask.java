package hu.unideb.smartcampus.task.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import hu.unideb.smartcampus.activity.login.LoginActivity;
import hu.unideb.smartcampus.activity.main.MainActivity;
import hu.unideb.smartcampus.dialog.loading.LoadingDialog;
import hu.unideb.smartcampus.pojo.login.ActualUserInfo;
import hu.unideb.smartcampus.task.pojo.ReturnPojo;
import hu.unideb.smartcampus.xmpp.Connection;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * LoginTask gets the Username, Password for ejabberd login
 * from restless call
 *
 * @see LoginActivity
 * @see ActualUserInfo
 * <p>
 * <p>
 * UnsafeOkHttpClient TODO
 * <p>
 * Created by Headswitcher on 2017. 03. 16..
 */

public class LoginTask extends AsyncTask<ActualUserInfo, Long, ReturnPojo> {

    private LoadingDialog loadingDialog; //TODO

    private Activity activity; //TODO

    public LoginTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        loadingDialog = new LoadingDialog();
        loadingDialog.show(activity.getFragmentManager(), "LOADING");
        super.onPreExecute();
    }

    @Override
    protected ReturnPojo doInBackground(ActualUserInfo... params) {
        ActualUserInfo userLoginDetails = params[0];
        int responseCode = BasicAuth.basicAuth(userLoginDetails);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseCode = Connection.establishConnection(userLoginDetails);
            return new ReturnPojo(responseCode);
        } else {
            return new ReturnPojo(responseCode);
        }
    }

    @Override
    protected void onPostExecute(ReturnPojo returnPojo) {
        super.onPostExecute(returnPojo);
        loadingDialog.dismiss();
        // TextView errorTextViewAtCustomNumberDialog = dialogFragment.getDialog().findViewById(R.id.custom_number_input_error_textview);
        switch (returnPojo.getStatusCode()) {
            case HTTP_OK:
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;

            case HTTP_BAD_REQUEST:
                //TODO SHOW ERROR
                break;

            //  case NO_INTERNET_ERROR:
            //     //TODO SHOW ERROR
            //    break;

            //     case UNKNOW_ERROR:
            //         //TODO SHOW ERROR
            //        break;

            case HTTP_GATEWAY_TIMEOUT:
                //TODO SHOW ERROR
                break;

            case HTTP_CLIENT_TIMEOUT:
                //TODO SHOW ERROR
                break;

            case HTTP_UNAUTHORIZED:
                //TODO SHOW ERROR

            case HTTP_INTERNAL_ERROR:
                //TODO SHOW ERROR
        }


    }
}

