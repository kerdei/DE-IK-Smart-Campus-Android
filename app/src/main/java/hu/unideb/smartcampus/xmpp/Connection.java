package hu.unideb.smartcampus.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.bosh.BOSHConfiguration;
import org.jivesoftware.smack.bosh.XMPPBOSHConnection;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import hu.unideb.smartcampus.pojo.login.ActualUserInfo;

import static java.lang.Thread.sleep;

public class Connection {

    private static Connection instance = new Connection();

    public static final String HTTP_BASIC_AUTH_PATH = "https://wt2.inf.unideb.hu/smartcampus-backend/integration/retrieveUserData";
    public static final String ADMINJID = "smartcampus@wt4.inf.unideb.hu/Smartcampus";
    public static final String HOSTNAME = "wt4.inf.unideb.hu";
    public static EntityJid adminEntityJID;

    private BOSHConfiguration config;
    private XMPPBOSHConnection xmppConnection;


    private Connection() {
        try {
            adminEntityJID = (EntityJid) JidCreate.from(ADMINJID);
        } catch (XmppStringprepException e) {
            //Should always crate Jid from this constant
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        return instance;
    }


    public static int establishConnection(ActualUserInfo actualUserInfo) {

        BOSHConfiguration config = null;
        try {
            config = BOSHConfiguration.builder()
                    .setUsernameAndPassword(actualUserInfo.getUsername(), actualUserInfo.getPassword())
                    .setXmppDomain(HOSTNAME)
                    .setHost("wt2.inf.unideb.hu")
                    .setPort(80)
                    .setFile("/http-bind/")
                    .setResource("Smartcampus")
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setDebuggerEnabled(false)
                    .build();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        Connection connection = Connection.getInstance();
        connection.setConfig(config);
        connection.maintainConnection();
        if (connection.xmppConnection.isAuthenticated()) {
            if (connection.xmppConnection.getConfiguration() != null) {
                connection.setUserJID(connection.xmppConnection.getConfiguration().toString());
            }
        }
        return 0; //TODO
    }

    //TODO
    private void maintainConnection() {
        xmppConnection = new XMPPBOSHConnection(config);
        try {
            if (!xmppConnection.isConnected()) {

                xmppConnection.connect();
                sleep(SmackConfiguration.getDefaultReplyTimeout());
                xmppConnection.login();
            } else if (!xmppConnection.isAuthenticated()) {
                xmppConnection.login();
            }
        } catch (InterruptedException | IOException | SmackException | XMPPException e) {
            e.printStackTrace();
        }
    }

    public XMPPBOSHConnection getXmppConnection() {
        return xmppConnection;
    }

    private void setUserJID(String userJID) {
    }

    private void setConfig(BOSHConfiguration config) {
        this.config = config;
    }
}
