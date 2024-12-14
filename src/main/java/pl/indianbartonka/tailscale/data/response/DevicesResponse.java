package pl.indianbartonka.tailscale.data.response;

import java.util.List;
import pl.indianbartonka.tailscale.data.response.device.Device;

public record DevicesResponse(List<Device> devices) {
}
