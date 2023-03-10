//package palestra.palestra;
//
//        import android.content.Context;
//
//import com.android.volley.Cache;
//import com.android.volley.Network;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//
///**
// * Created by Dmitry on 19.04.2017.
// */
//
//public class VolleyRequest {
//    private static VolleyRequest volleyRequest;
//    private static Context context;
//    private RequestQueue requestQueue;
//
//    private VolleyRequest(Context context) {
//        this.context = context;
//        this.requestQueue = getRequestQueue();
//    }
//
//    public static synchronized VolleyRequest getInstance(Context context) {
//        if (volleyRequest == null) {
//            volleyRequest  = new VolleyRequest(context);
//        }
//        return volleyRequest;
//    }
//
//    public RequestQueue getRequestQueue() {
//        if (requestQueue == null) {
//            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
//            Network network = new BasicNetwork(new HurlStack());
//            requestQueue = new RequestQueue(cache, network);
//            requestQueue.start();
//        }
//        return requestQueue;
//    }
//}
