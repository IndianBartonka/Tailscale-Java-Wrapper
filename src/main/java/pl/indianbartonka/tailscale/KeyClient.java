package pl.indianbartonka.tailscale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import pl.indianbartonka.tailscale.data.request.ApiKeyRequest;
import pl.indianbartonka.tailscale.data.response.key.ApiKey;
import pl.indianbartonka.tailscale.data.response.key.ApiKeyResponse;
import pl.indianbartonka.tailscale.data.response.key.ApiKeysResponse;
import pl.indianbartonka.util.http.ContentType;
import pl.indianbartonka.util.http.connection.Connection;
import pl.indianbartonka.util.http.connection.request.Request;
import pl.indianbartonka.util.http.connection.request.RequestBody;
import pl.indianbartonka.util.http.connection.request.RequestBuilder;

public class KeyClient {

    private final TailscaleClient tailscaleClient;

    public KeyClient(final TailscaleClient tailscaleClient) {
        this.tailscaleClient = tailscaleClient;
    }

    public List<ApiKey> getKeys() throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/tailnet/" + this.tailscaleClient.getOrganizationName() + "/keys")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, ApiKeysResponse.class).keys();
        }
    }

    public ApiKeyResponse createKey(final ApiKeyRequest apiKeyRequest) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/tailnet/" + this.tailscaleClient.getOrganizationName() + "/keys")
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .setContentType(ContentType.APPLICATION_JSON)
                .POST(new RequestBody(this.tailscaleClient.getGson().toJson(apiKeyRequest).getBytes(StandardCharsets.UTF_8)))
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, ApiKeyResponse.class);
        }
    }

    public ApiKeyResponse getKey(final String keyId) throws IOException {
    final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/tailnet/" + this.tailscaleClient.getOrganizationName() + "/keys/" + keyId)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .setContentType(ContentType.APPLICATION_JSON)
                .GET()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);

            return this.tailscaleClient.getGson().fromJson(body, ApiKeyResponse.class);
        }
    }

    public void deleteKey(final String keyId) throws IOException {
        final Request request = new RequestBuilder()
                .setUrl(this.tailscaleClient.getBaseUrl() + "/tailnet/" + this.tailscaleClient.getOrganizationName() + "/keys/" + keyId)
                .setAuthorization("Bearer " + this.tailscaleClient.getToken())
                .setContentType(ContentType.APPLICATION_JSON)
                .DELETE()
                .build();

        try (final Connection connection = new Connection(request)) {
            final String body = connection.getBodyAsString();

            if (!connection.isSuccessful()) this.tailscaleClient.handleError(connection.getRawStatusCode(), body);
        }
    }
}
