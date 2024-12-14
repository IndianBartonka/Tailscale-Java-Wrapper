package pl.indianbartonka.tailscale.data.response.device;

import java.util.List;
import java.util.Map;

public record ClientConnectivity(
        List<String> endpoints,
        Map<String, Latency> latency,
        boolean mappingVariesByDestIP,
        ClientSupports clientSupports
) {
}