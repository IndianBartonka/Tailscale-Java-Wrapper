package pl.indianbartonka.tailscale.data.response;

import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record AttributesResponse(Map<String, String> attributes, @Nullable Map<String, String> expiries) {
}
