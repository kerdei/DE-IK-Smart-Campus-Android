package hu.unideb.smartcampus.task.login;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.Arrays;

import hu.unideb.smartcampus.pojo.login.ActualUserInfo;
import hu.unideb.smartcampus.unsafe.Unsafe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

/**
 * Makes a basic authorization with the backend.
 */
class BasicAuth {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String HTTP_BASIC_AUTH_URL = "localhost:8080/smartcampus-backend/retrieveUserData";

    /**
     * Creates a http basic auth request and returns the http code of the response
     *
     * @param paramActualUserInfo user's login information which was provided at the login screen
     * @return HTTP Status-Code of the response
     */
    static int basicAuth(ActualUserInfo paramActualUserInfo) {
        int responseCode;

        OkHttpClient httpClient = Unsafe.getUnsafeOkHttpClient();

        ActualUserInfo actualUserInfo = paramActualUserInfo;
        String toBase64 = actualUserInfo.getUsername() + ":" + actualUserInfo.getPassword();
        String encodedUsernameAndPassword = Arrays.toString(Base64.encodeBase64(toBase64.getBytes()));

        Request request = new Request.Builder()
                .url(HTTP_BASIC_AUTH_URL)
                .header(AUTHORIZATION_HEADER, "Basic " + encodedUsernameAndPassword)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            responseCode = response.code();
        } catch (IOException e) {
            return HTTP_INTERNAL_ERROR;
        }
        return responseCode;
    }

}

