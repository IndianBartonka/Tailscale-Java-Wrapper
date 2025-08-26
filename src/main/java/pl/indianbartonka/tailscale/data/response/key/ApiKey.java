package pl.indianbartonka.tailscale.data.response.key;

public record ApiKey(
        String id,
        String keyType,
        long expirySeconds,
        String created,
        String expires,
        Capabilities capabilities,
        String description,
        String userId
) {
}
