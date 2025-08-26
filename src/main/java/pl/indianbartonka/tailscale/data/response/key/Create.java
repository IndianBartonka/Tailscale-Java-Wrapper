package pl.indianbartonka.tailscale.data.response.key;

import java.util.List;

public record Create(
        boolean reusable,
        boolean ephemeral,
        boolean preauthorized,
        List<String> tags
) {
}