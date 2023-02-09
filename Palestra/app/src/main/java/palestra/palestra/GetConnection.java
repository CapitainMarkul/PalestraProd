package palestra.palestra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Dmitry on 14.04.2017.
 */

public class GetConnection extends Activity {

    private static GetConnection instance;

    private GetConnection() {
    }

    public static synchronized GetConnection getInstance(){
        if(instance==null){
            instance = new GetConnection();
        }
        return instance;
    }

    public boolean isOnline(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public boolean isOnline(final Context context, final View content) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        Snackbar snackbar = Snackbar
                .make(content, "Отсутствует соединение", Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isOnline(context,content);
                    }
                });
        snackbar.setActionTextColor(Color.CYAN);
        snackbar.show();
        return false;
    }
    private boolean isShow = false;
    public boolean isShow(){
        return isShow;
    }
    public boolean isOnlineMap(final Context context, final View content) {
        Snackbar snackbar = Snackbar
                .make(content, "Отсутствует соединение", Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isOnline(context,content);
                    }
                });
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            isShow = false;
            snackbar.dismiss();
            return true;
        }
        isShow = true;
        snackbar.setActionTextColor(Color.CYAN);
        snackbar.show();
        return false;
    }
}
