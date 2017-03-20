package hu.unideb.smartcampus.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.bosh.BOSHConfiguration;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import hu.unideb.smartcampus.R;
import hu.unideb.smartcampus.fragment.LoadingDialogFragment;
import hu.unideb.smartcampus.main.activity.login.pojo.ActualUserInfo;
import hu.unideb.smartcampus.xmpp.Connection;

import static hu.unideb.smartcampus.xmpp.Connection.HOSTNAME;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void login() {
        setupVariables();
        ActualUserInfo actualUserInfo = null;
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.usernamePasswordNeed, Toast.LENGTH_SHORT).show();
        } else {
            /*
            try {
                actualUserInfo = new BasicAuth().execute(new ActualUserInfo(username.getText().toString(), password.getText().toString(), null)).get(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }*/
            /*if (actualUserInfo.getUserName() == null) {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();*/
            //} else {
                Toast.makeText(getApplicationContext(), R.string.loginSucces, Toast.LENGTH_SHORT).show();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.activity_login, new LoadingDialogFragment());
                fragmentTransaction.commitAllowingStateLoss();

                new Thread(new Runnable() {
                    public void run() {
                        BOSHConfiguration config = null;
                        try {
                            config = BOSHConfiguration.builder()
                                    //.setUsernameAndPassword(username.getText().toString(), password.getText().toString())
                                    .setUsernameAndPassword("testuser","admin")
                                    .setXmppDomain(JidCreate.domainBareFrom(HOSTNAME))
                                    .setHost(HOSTNAME)
                                    .setPort(80)
                                    .setFile("/http-bind/")
                                    .build();
                        } catch (XmppStringprepException e) {
                            e.printStackTrace();
                        }
                        Connection.getInstance().startBoshConnection(config, getApplicationContext());

                    }

                }).start();
            }
        //}
    }

    public void loginOnClick(View v) {
        login();
    }

    private void setupVariables() {
        username = (EditText) findViewById(R.id.usernameId);
        password = (EditText) findViewById(R.id.passwordId);
    }

}

