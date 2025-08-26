package pl.indianbartonka.tailscale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import pl.indianbartonka.tailscale.data.request.AttributesRequest;
import pl.indianbartonka.tailscale.data.request.AuthorizedRequest;
import pl.indianbartonka.tailscale.data.request.IpV4Request;
import pl.indianbartonka.tailscale.data.request.KeyExpiryDisabledRequest;
import pl.indianbartonka.tailscale.data.request.NameRequest;
import pl.indianbartonka.tailscale.data.request.RoutesRequest;
import pl.indianbartonka.tailscale.data.request.TagsRequest;
import pl.indianbartonka.tailscale.data.response.AttributesResponse;
import pl.indianbartonka.tailscale.data.response.RoutesResponse;
import pl.indianbartonka.tailscale.data.response.device.Device;
import pl.indianbartonka.tailscale.data.response.device.DevicesResponse;
import pl.indianbartonka.util.http.ContentType;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBody;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;

public class DeviceClient {

    private final TailscaleClient tailscaleClient;

    public DeviceClient(final TailscaleClient tailscaleClient) {
        this.tailscaleClient = tailscaleClient;
    }

    public List<Device> getDevices() throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/tailnet/" + this.tailscaleClient.getOrganizationName() + "/devices")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, DevicesResponse.class).devices();
        }
    }

    public Device getDevice(final String deviceId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, Device.class);
        }
    }

    public boolean deleteDevice(final String deviceId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .DELETE()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return true;
        }
    }

    public boolean setExpire(final String deviceId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/expire")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody("".getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public RoutesResponse getRoutes(final String deviceId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/routes")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, RoutesResponse.class);
        }
    }

    public RoutesResponse setRoutes(final String deviceId, final RoutesRequest routesRequest) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/routes")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(routesRequest).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, RoutesResponse.class);
        }
    }

    public boolean setAuthorized(final String deviceId, final boolean authorized) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/authorized")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(new AuthorizedRequest(authorized)).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public boolean setName(final String deviceId, final String name) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/name")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(new NameRequest(name)).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    /**
     * I don't know how to use tags
     */
    public boolean setTags(final String deviceId, final Map<String, String> tags) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/tags")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(new TagsRequest(tags)).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public boolean setKeyExpiryDisabled(final String deviceId, final boolean keyExpiryDisabled) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/key")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(new KeyExpiryDisabledRequest(keyExpiryDisabled)).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public boolean setIpV4(final String deviceId, final String ipV4) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/ip")
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(new IpV4Request(ipV4)).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public boolean setAttributes(final String deviceId, final String attributeKey, final AttributesRequest attributesRequest) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/attributes/" + attributeKey)
                .setContentType(ContentType.APPLICATION_JSON)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(attributesRequest).getBytes(StandardCharsets.UTF_8)))
                .build();

        System.out.println(this.tailscaleClient.getGson().toJson(attributesRequest));

        try (final Connection connection = new Connection(request)) {

            if (!connection.isSuccessful())
                this.tailscaleClient.handleError(connection.getRawStatusCode(), connection.getBodyAsString());

            return true;
        }
    }

    public boolean deleteAttributes(final String deviceId, final String attributeKey) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/attributes/" + attributeKey)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .DELETE()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return true;
        }
    }

    public AttributesResponse getAttributes(final String deviceId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/device/" + deviceId + "/attributes")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, AttributesResponse.class);
        }
    }
}
