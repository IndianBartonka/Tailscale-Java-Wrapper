package pl.indianbartonka.tailscale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pl.indianbartonka.tailscale.data.request.AttributesRequest;
import pl.indianbartonka.tailscale.data.request.RoutesRequest;
import pl.indianbartonka.tailscale.data.response.AttributesResponse;
import pl.indianbartonka.tailscale.data.response.device.Device;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.MessageUtil;

public class TailscaleClientTest {

    public static void main(final String[] args) throws IOException {
        final TailscaleClient client = new TailscaleClient(EnvironmentReader.getEnvironment("orgName"), EnvironmentReader.getEnvironment("apiKey"));

        // You can also set a custom OkHttp client, Gson client, and a custom BaseUrl
        // TailscaleClient#setHttpClient
        // TailscaleClient#setGson
        // TailscaleClient#setBaseUrl

        // Getting all network devices
        final List<Device> devices = client.getDevices();

        final String deviceId = devices.get(0).id();
        final Device device = client.getDevice(deviceId);

        System.out.println();
        System.out.println();

        // Example of expiring a device
        System.out.println("Device expired: " + client.setExpire(deviceId));

        // Example of changing the authorization status
        System.out.println("Authorization changed: " + client.setAuthorized(deviceId, true));

        // Example of changing the device name
        System.out.println("Device name changed: " + client.setName(deviceId, "Device " + MessageUtil.generateCode(10)));

        // Example of setting tags
        System.out.println("Tags set: " + client.setTags(deviceId, Map.of("tagKey", "tagValue")));

        // Example of disabling key expiry
        System.out.println("Key expiry disabled: " + client.setKeyExpiryDisabled(deviceId, true));

        // Example of setting an IP address
        System.out.println("IPv4 set: " + client.setIpV4(deviceId, "100.80.0.1"));

        // Example of fetching device attributes
        final AttributesResponse attributesResponse = client.getAttributes(deviceId);
        System.out.println("Attributes: " + attributesResponse);

        // Example of setting an attribute on the device
        final AttributesRequest attributesRequest = new AttributesRequest("value", "2022-12-01T05:23:30Z");
        final boolean isSet = client.setAttributes(deviceId, "custom:Koo", attributesRequest);
        System.out.println("Attribute set: " + isSet);

        // Example of deleting an attribute from the device
        final boolean isDeleted = client.deleteAttributes(deviceId, "custom:Koo");
        System.out.println("Attribute deleted: " + isDeleted);

        // Example of working with routes
        System.out.println(client.getRoutes(deviceId));
        System.out.println(client.setRoutes(deviceId, new RoutesRequest(List.of("10.0.0.0/16", "192.168.1.0/24"))));
        System.out.println(client.getRoutes(deviceId));

        // Example of deleting a device
        System.out.println("Device deleted: " + client.deleteDevice(deviceId));
    }
}

class EnvironmentReader {

    private static File environmentFile = new File(".env");

    private static final Map<String, String> keys = new LinkedHashMap<>();

    public static void read() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(environmentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    final String value = parts[1].trim().replace("\\n", "\n").replace("\\t", "\t");

                    keys.put(parts[0].trim(), value);
                }
            }
        } catch (final IOException ioException) {
            if (IndianUtils.debug) ioException.printStackTrace();
        }
    }

    public static String getEnvironment(final String key) {
        if (keys.isEmpty()) read();

        return keys.get(key);
    }

    public static void setEnvironmentFile(final File environmentFile) {
        EnvironmentReader.environmentFile = environmentFile;
    }
}