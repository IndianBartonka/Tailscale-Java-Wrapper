package pl.indianbartonka.tailscale.data.response.key;

import java.util.List;

public record ApiKeyResponse(
        String id,
        String key,
        String keyType,
        long expirySeconds,
        String created,
        String expires,
        String revoked,
        Capabilities capabilities,
        List<String> scopes,
        List<String> tags,
        String description,
        boolean invalid,
        String userId
) {

}
