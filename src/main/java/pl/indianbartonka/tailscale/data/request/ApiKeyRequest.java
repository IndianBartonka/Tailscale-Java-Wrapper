package pl.indianbartonka.tailscale.data.request;

import java.util.List;
import pl.indianbartonka.tailscale.data.response.key.Capabilities;

public record ApiKeyRequest(
        String description,
        Capabilities capabilities,
        long expirySeconds,
        List<String> scopes,
        List<String> tags
) {
}