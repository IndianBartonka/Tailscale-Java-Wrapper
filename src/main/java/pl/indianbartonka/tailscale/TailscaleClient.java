package pl.indianbartonka.tailscale;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.indianbartonka.tailscale.data.request.AttributesRequest;
import pl.indianbartonka.tailscale.data.request.AuthorizedRequest;
import pl.indianbartonka.tailscale.data.request.IpV4Request;
import pl.indianbartonka.tailscale.data.request.KeyExpiryDisabledRequest;
import pl.indianbartonka.tailscale.data.request.NameRequest;
import pl.indianbartonka.tailscale.data.request.RoutesRequest;
import pl.indianbartonka.tailscale.data.request.TagsRequest;
import pl.indianbartonka.tailscale.data.response.AttributesResponse;
import pl.indianbartonka.tailscale.data.response.DevicesResponse;
import pl.indianbartonka.tailscale.data.response.ErrorMessage;
import pl.indianbartonka.tailscale.data.response.RoutesResponse;
import pl.indianbartonka.tailscale.data.response.device.Device;
import pl.indianbartonka.tailscale.exception.TailscaleException;
import pl.indianbartonka.tailscale.util.HTTPUtil;
import pl.indianbartonka.util.GsonUtil;
import pl.indianbartonka.util.http.HttpStatusCode;

public class TailscaleClient {

    private final String organizationName;
    private final String token;
    private String baseUrl;
    private OkHttpClient httpClient;
    private Gson gson;

    public TailscaleClient(final String organizationName, final String token) {
        this.organizationName = organizationName;
        this.token = token;
        this.baseUrl = "https://api.tailscale.com/api/v2";
        this.httpClient = HTTPUtil.getOkHttpClient();
        this.gson = GsonUtil.getGson();
    }

    public List<Device> getDevices() throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/tailnet/" + this.organizationName + "/devices")
                .addHeader("Authorization", "Bearer " + this.token)
                .get()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return this.gson.fromJson(responseMessage, DevicesResponse.class).devices();
        }
    }

    public Device getDevice(final String deviceId) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId)
                .addHeader("Authorization", "Bearer " + this.token)
                .get()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return this.gson.fromJson(responseMessage, Device.class);
        }
    }

    public boolean deleteDevice(final String deviceId) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId)
                .addHeader("Authorization", "Bearer " + this.token)
                .delete()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return true;
        }
    }

    public boolean setExpire(final String deviceId) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/expire")
                .addHeader("Authorization", "Bearer " + this.token)
                .post(RequestBody.create("", MediaType.parse("text/plain")))
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public RoutesResponse getRoutes(final String deviceId) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/routes")
                .addHeader("Authorization", "Bearer " + this.token)
                .get()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return this.gson.fromJson(responseMessage, RoutesResponse.class);
        }
    }

    public RoutesResponse setRoutes(final String deviceId, final RoutesRequest routesRequest) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(routesRequest), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/routes")
                .addHeader("Authorization", "Bearer " + this.token)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return this.gson.fromJson(responseMessage, RoutesResponse.class);
        }
    }

    public boolean setAuthorized(final String deviceId, final boolean authorized) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(new AuthorizedRequest(authorized)), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/authorized")
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public boolean setName(final String deviceId, final String name) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(new NameRequest(name)), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/name")
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    /**
     * I don't know how to use tags
     */
    public boolean setTags(final String deviceId, final Map<String, String> tags) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(new TagsRequest(tags)), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/tags")
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public boolean setKeyExpiryDisabled(final String deviceId, final boolean keyExpiryDisabled) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(new KeyExpiryDisabledRequest(keyExpiryDisabled)), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/key")
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public boolean setIpV4(final String deviceId, final String ipV4) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(new IpV4Request(ipV4)), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/ip")
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public boolean setAttributes(final String deviceId, final String attributeKey, final AttributesRequest attributesRequest) throws IOException {
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(this.gson.toJson(attributesRequest), mediaType);

        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/attributes/" + attributeKey)
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Authorization", "Bearer " + this.token)
                .post(body)
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) this.handleError(response.code(), response.body().string());

            return true;
        }
    }

    public boolean deleteAttributes(final String deviceId, final String attributeKey) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/attributes/" + attributeKey)
                .addHeader("Authorization", "Bearer " + this.token)
                .delete()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return true;
        }
    }

    public AttributesResponse getAttributes(final String deviceId) throws IOException {
        final Request request = new Request.Builder()
                .url(this.baseUrl + "/device/" + deviceId + "/attributes")
                .addHeader("Authorization", "Bearer " + this.token)
                .get()
                .build();

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseMessage = response.body().string();

            if (!response.isSuccessful()) this.handleError(response.code(), responseMessage);

            return this.gson.fromJson(responseMessage, AttributesResponse.class);
        }
    }

    private void handleError(final int code, final String responseMessage) {
        final String error;
        final ErrorMessage errorMessage = this.gson.fromJson(responseMessage, ErrorMessage.class);

        if (errorMessage != null) {
            error = errorMessage.message();
        } else {
            error = HttpStatusCode.getByCode(code).name();
        }

        throw new TailscaleException(error);
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setHttpClient(final OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setGson(final Gson gson) {
        this.gson = gson;
    }
}
