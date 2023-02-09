package palestra.palestra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Dmitry on 28.04.2017.
 */

class HttpGetAddress {

    private static HttpGetAddress instance;

    private HttpGetAddress(){
    }

    public static synchronized HttpGetAddress getInstance(){
        if(instance==null){
            instance = new HttpGetAddress();
        }
        return instance;
    }

    public final String SEARCH_ADDRESS = "Определение адреса...";
    public final String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public final String KEY_API = "&key=AIzaSyCnpjSiv6cx8UTBfP2L4tvOtCInp4hwoSo";
    public final String LANGUAGE_REQUEST = "&language=ru";

    public String SendRequest(String requestURL){
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
           //conn.setRequestProperty();
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
