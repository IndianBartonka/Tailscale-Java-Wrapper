package pl.indianbartonka.tailscale.data.response;

import java.util.List;

public record RoutesResponse(List<String> advertisedRoutes, List<String> enabledRoutes) {
}