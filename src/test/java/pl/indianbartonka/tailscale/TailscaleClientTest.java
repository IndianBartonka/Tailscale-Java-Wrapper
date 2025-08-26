package pl.indianbartonka.tailscale;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import pl.indianbartonka.tailscale.data.request.ApiKeyRequest;
import pl.indianbartonka.tailscale.data.request.AttributesRequest;
import pl.indianbartonka.tailscale.data.request.RoutesRequest;
import pl.indianbartonka.tailscale.data.response.AttributesResponse;
import pl.indianbartonka.tailscale.data.response.device.Device;
import pl.indianbartonka.tailscale.data.response.key.ApiKey;
import pl.indianbartonka.tailscale.data.response.key.ApiKeyResponse;
import pl.indianbartonka.tailscale.data.response.key.Capabilities;
import pl.indianbartonka.tailscale.data.response.key.Create;
import pl.indianbartonka.tailscale.data.response.key.Devices;
import pl.indianbartonka.tailscale.exception.TailscaleException;
import pl.indianbartonka.util.DateUtil;
import pl.indianbartonka.util.EnvironmentReader;
import pl.indianbartonka.util.MessageUtil;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;

public final class TailscaleClientTest {

    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
            .setLoggingToFile(false)
            .build();

    private static final Logger LOGGER = new Logger(loggerConfiguration) {
    };

    public static void main(final String[] args) throws IOException {
        final EnvironmentReader environment = new EnvironmentReader(new File(".env"));
        environment.read();

        final TailscaleClient client = new TailscaleClient(environment.getEnvironment("orgName"), environment.getEnvironment("apiKey"));

        // You can also set a custom Gson client, and a custom BaseUrl just in case
        // TailscaleClient#setGson
        // TailscaleClient#setBaseUrl

        keyExamples(client.getKeyClient(), client.getGson());
        deviceExamples(client.getDeviceClient());
    }

    private static void keyExamples(final KeyClient client, final Gson gson) throws IOException {
        final ApiKeyRequest request = new ApiKeyRequest(
                "dev access",
                new Capabilities(
                        new Devices(new Create(true, false, true, List.of("tag:example")))
                ),
                DateUtil.daysTo(1, TimeUnit.SECONDS),
                List.of("all:read"),
                List.of("tag:example")
        );

        final ApiKeyResponse key1 = client.createKey(request);
        LOGGER.info(key1);
        LOGGER.info(gson.toJson(key1));
        LOGGER.println();
        LOGGER.println();

        final ApiKeyResponse key2 = client.getKey("kQ8df7Cjn821CNTRL");
        LOGGER.info(key2);
        LOGGER.info(gson.toJson(key2));
        LOGGER.println();
        LOGGER.println();

        final List<ApiKey> keyList = client.getKeys();

        LOGGER.println();
        LOGGER.println();
        LOGGER.info(keyList.size());
        LOGGER.info(keyList);

        for (final ApiKey key : keyList) {
            LOGGER.info(key);
            LOGGER.info(gson.toJson(key));
        }

        client.deleteKey(key1.id());

        final List<ApiKey> keyList2 = client.getKeys();

        LOGGER.println();
        LOGGER.println();
        LOGGER.info(keyList2.size());
        LOGGER.info(keyList2);

        for (final ApiKey key : keyList2) {
            LOGGER.info(key);
            LOGGER.info(gson.toJson(key));
        }
    }

    private static void deviceExamples(final DeviceClient client) throws IOException {
        // Getting all network devices
        final List<Device> devices = client.getDevices();

        final String deviceId = devices.get(0).id();
        final Device device = client.getDevice(deviceId);

        LOGGER.info(device.name());

        LOGGER.println();
        LOGGER.println();

        // Example of expiring a device
        LOGGER.info("Device expired: " + client.setExpire(deviceId));

        // Example of changing the authorization status
        LOGGER.info("Authorization changed: " + client.setAuthorized(deviceId, true));

        // Example of changing the device name
        LOGGER.info("Device name changed: " + client.setName(deviceId, "Device " + MessageUtil.generateCode(10)));

        try {
            // Example of setting tags
            LOGGER.info("Tags set: " + client.setTags(deviceId, Map.of("tagKey", "tagValue")));
        } catch (final TailscaleException tailscaleException) {
            LOGGER.error(tailscaleException.getMessage(), tailscaleException);
        }

        // Example of disabling key expiry
        LOGGER.info("Key expiry disabled: " + client.setKeyExpiryDisabled(deviceId, true));

        try {
            // Example of setting an IP address
            LOGGER.info("IPv4 set: " + client.setIpV4(deviceId, "100.80.0.2"));
        } catch (final TailscaleException tailscaleException) {
            LOGGER.error(tailscaleException.getMessage(), tailscaleException);
        }

        // Example of fetching device attributes
        final AttributesResponse attributesResponse = client.getAttributes(deviceId);
        LOGGER.info("Attributes: " + attributesResponse);

        try {
            // Example of setting an attribute on the device
            final AttributesRequest attributesRequest = new AttributesRequest("value", "2022-12-01T05:23:30Z");
            final boolean isSet = client.setAttributes(deviceId, "custom:Koo", attributesRequest);
            LOGGER.info("Attribute set: " + isSet);

            // Example of deleting an attribute from the device
            final boolean isDeleted = client.deleteAttributes(deviceId, "custom:Koo");
            LOGGER.info("Attribute deleted: " + isDeleted);
        } catch (final TailscaleException tailscaleException) {
            LOGGER.error("Tailscale &cCOŚ SPIERDOLILI DEBILE");
            LOGGER.error(tailscaleException.getMessage(), tailscaleException);
        }

        // Example of working with routes
        LOGGER.info(client.getRoutes(deviceId));
        LOGGER.info(client.setRoutes(deviceId, new RoutesRequest(List.of("10.0.0.0/16", "192.168.1.0/24"))));
        LOGGER.info(client.getRoutes(deviceId));

        // Example of deleting a device
        LOGGER.info("Device deleted: " + client.deleteDevice(deviceId));
    }
}