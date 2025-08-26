package pl.indianbartonka.tailscale.data.response.device;

import java.util.List;

public record DevicesResponse(List<Device> devices) {
}
