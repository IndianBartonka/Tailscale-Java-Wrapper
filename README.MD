> [!IMPORTANT]
> We currently support <br>
> [Devices](https://tailscale.com/api#tag/devices) <br>
> [Keys](https://tailscale.com/api#tag/keys/get/tailnet/{tailnet}/keys)
>
>

# Tailscale Java Wrapper

A wrapper for the Tailscale API. For more information, visit the
official [Tailscale API documentation](https://tailscale.com/api).

This example demonstrates how to use the `TailscaleClient` in Java to interact with the Tailscale API. It covers
retrieving device details, updating device attributes, managing routes, and more.

## Prerequisites

- A Tailscale account.
- A valid Tailscale [API key](https://login.tailscale.com/admin/settings/keys).

# Dependencies

### Maven `pom.xml`

```xml

<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml

<dependency>
    <groupId>com.github.IndianBartonka</groupId>
    <artifactId>Tailscale-Java-Wrapper</artifactId>
    <version>Tag</version>
</dependency>
```

## Example Usage

[TailscaleClientTest.java](src/test/java/pl/indianbartonka/tailscale/TailscaleClientTest.java)

