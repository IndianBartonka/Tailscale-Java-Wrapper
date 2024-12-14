package pl.indianbartonka.tailscale.exception;

public class TailscaleException extends RuntimeException {

    public TailscaleException(final String message) {
        super(message);
    }
}
