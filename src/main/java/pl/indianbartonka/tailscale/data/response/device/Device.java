package pl.indianbartonka.tailscale.data.response.device;

import java.util.List;
import java.util.Map;

public record Device(
        List<String> addresses,
        boolean authorized,
        boolean blocksIncomingConnections,
        String clientVersion,
        String created,
        String expires,
        String hostname,
        String id,
        boolean isExternal,
        boolean keyExpiryDisabled,
        String lastSeen,
        String machineKey,
        String name,
        String nodeId,
        String nodeKey,
        String os,
        List<String> enabledRoutes,
        List<String> advertisedRoutes,
        ClientConnectivity clientConnectivity,
        List<String> tags,
        String tailnetLockError,
        String tailnetLockKey,
        PostureIdentity postureIdentity,
        boolean updateAvailable,
        String user
) {

    public record Latency(Double latencyMs, Boolean preferred) {
    }

    public record ClientConnectivity(
            List<String> endpoints,
            Map<String, Latency> latency,
            boolean mappingVariesByDestIP,
            ClientSupports clientSupports
    ) {
    }

    public record ClientSupports(
            boolean hairPinning,
            boolean ipv6,
            boolean pcp,
            boolean pmp,
            boolean udp,
            boolean upnp
    ) {
    }

    public record PostureIdentity(List<String> serialNumbers) {
    }
}