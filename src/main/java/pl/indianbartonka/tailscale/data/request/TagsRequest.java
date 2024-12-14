package pl.indianbartonka.tailscale.data.request;

import java.util.Map;

public record TagsRequest(Map<String, String> tags) {
}
