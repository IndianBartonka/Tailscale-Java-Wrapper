package pl.indianbartonka.tailscale.data.response.key;

import java.util.List;

public record ApiKeysResponse(List<ApiKey> keys) {
}
