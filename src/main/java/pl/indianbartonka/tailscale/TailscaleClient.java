package pl.indianbartonka.tailscale;

import com.google.gson.Gson;
import java.net.http.HttpClient;
import pl.indianbartonka.tailscale.data.response.ErrorMessage;
import pl.indianbartonka.tailscale.exception.TailscaleException;
import pl.indianbartonka.util.GsonUtil;
import pl.indianbartonka.util.http.HttpStatusCode;

public class TailscaleClient {

    private final String organizationName;
    private final String token;
    private final DeviceClient deviceClient;
    private final KeyClient keyClient;
    private final DNSClient dnsClient;
    private String baseUrl;
    private Gson gson;

    public TailscaleClient(final String organizationName, final String token) {
        this.organizationName = organizationName;
        this.token = token;
        this.baseUrl = "https://api.tailscale.com/api/v2";
        this.gson = GsonUtil.getGson();
        this.deviceClient = new DeviceClient(this);
        this.keyClient = new KeyClient(this);
        this.dnsClient = new DNSClient();
    }

    public void handleError(final int code, final String body) {
        final String error;
        final ErrorMessage errorMessage = this.getGson().fromJson(body, ErrorMessage.class);

        if (errorMessage != null) {
            error = errorMessage.message();
        } else {
            error = HttpStatusCode.getByCode(code).name();
        }

        throw new TailscaleException(error);
    }

    public DeviceClient getDeviceClient() {
        return this.deviceClient;
    }

    public KeyClient getKeyClient() {
        return this.keyClient;
    }

    public DNSClient getDnsClient() {
        return this.dnsClient;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public String getToken() {
        return this.token;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Gson getGson() {
        return this.gson;
    }

    public void setGson(final Gson gson) {
        this.gson = gson;
    }
}
