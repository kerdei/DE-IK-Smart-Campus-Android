package hu.unideb.smartcampus.activity.login;

import android.os.AsyncTask;

import java.net.HttpURLConnection;

import hu.unideb.smartcampus.activity.login.BasicAuth;
import hu.unideb.smartcampus.activity.login.LoginActivity;
import hu.unideb.smartcampus.pojo.login.ActualUserInfo;
import hu.unideb.smartcampus.xmpp.Connection;

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

public class LoginTask extends AsyncTask<ActualUserInfo, Long, Integer> {

    private AsyncResponse delegate;

    interface AsyncResponse {
        void processFinish(int httpResponseCode);
    }

    LoginTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Integer doInBackground(ActualUserInfo... params) {
        ActualUserInfo userLoginDetails = params[0];
        int responseCode = BasicAuth.basicAuth(userLoginDetails);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseCode = Connection.establishConnection(userLoginDetails);
        }
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer httpResponseCode) {
        delegate.processFinish(httpResponseCode);
    }
}

