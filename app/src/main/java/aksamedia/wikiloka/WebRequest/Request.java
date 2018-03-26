package aksamedia.wikiloka.WebRequest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by fandi on 11/20/2016.
 */

public class Request {
    private static final String BASE_URL = "http://wikiloka.com/api_v1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        String URL = getAbsoluteUrl(url);
        client.get(URL, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        String URL = getAbsoluteUrl(url);
        client.post(URL, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
