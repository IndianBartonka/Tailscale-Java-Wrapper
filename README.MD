# Tailscale Java Wrapper

A wrapper for the Tailscale API. For more information, visit the official [Tailscale API documentation](https://tailscale.com/api).

This example demonstrates how to use the `TailscaleClient` in Java to interact with the Tailscale API. It covers retrieving device details, updating device attributes, managing routes, and more.

## Prerequisites

- A Tailscale account.
- A valid Tailscale [API key](https://login.tailscale.com/admin/settings/keys).

## Example Usage

```java
final TailscaleClient client = new TailscaleClient("Organization Name", "API Token");

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
final AttributesRequest attributesRequest = new AttributesRequest("value", "my_value");
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
