package pl.indianbartonka.tailscale.data.response.device;

public record ClientSupports(
        boolean hairPinning,
        boolean ipv6,
        boolean pcp,
        boolean pmp,
        boolean udp,
        boolean upnp
) {
}