package pl.indianbartonka.tailscale.util;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public final class HTTPUtil {

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .protocols(List.of(Protocol.HTTP_3, Protocol.HTTP_2, Protocol.HTTP_1_1))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
            .followRedirects(true)
            .build();

    private HTTPUtil() {
    }

    public static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }
}
