package pkg1.VETCAI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LedService {
    private final String esp32Url;
    private final HttpClient httpClient;
    private final Duration timeout;
    private final String method;
    private final String path;
    private final String apiKeyHeader;
    private final String apiKeyValue;

    public LedService(@Value("${esp32.url:}") String esp32Url,
                      @Value("${esp32.timeout-seconds:5}") int timeoutSeconds,
                      @Value("${esp32.method:GET}") String method,
                      @Value("${esp32.path:/led}") String path,
                      @Value("${esp32.api-key-header:}") String apiKeyHeader,
                      @Value("${esp32.api-key-value:}") String apiKeyValue) {
        this.esp32Url = esp32Url == null ? "" : esp32Url.trim();
        this.timeout = Duration.ofSeconds(Math.max(1, timeoutSeconds));
        this.httpClient = HttpClient.newBuilder().connectTimeout(this.timeout).build();
        this.method = method == null ? "GET" : method.trim().toUpperCase(Locale.ROOT);
        this.path = path == null ? "/led" : path.trim();
        this.apiKeyHeader = apiKeyHeader == null ? "" : apiKeyHeader.trim();
        this.apiKeyValue = apiKeyValue == null ? "" : apiKeyValue.trim();
    }

    public boolean isConfigured() {
        return esp32Url != null && !esp32Url.isEmpty();
    }

    /**
     * Send a simple command to ESP32.
     * Assumes ESP32 can accept a GET request like: {esp32Url}/led?color=red&state=on
     */
    public boolean sendCommand(String color, boolean on) {
        if (!isConfigured()) return false;
        String c = color == null ? "" : color.toLowerCase(Locale.ROOT);
        if (!(c.equals("red") || c.equals("yellow") || c.equals("green"))) return false;

        String state = on ? "on" : "off";
        try {
            String base = esp32Url.replaceAll("/+$", "");
            String p = path.startsWith("/") ? path : "/" + path;
            HttpRequest.Builder rb;

            if ("POST".equals(method)) {
                String json = String.format("{\"color\":\"%s\",\"state\":\"%s\"}", c, state);
                rb = HttpRequest.newBuilder()
                        .uri(URI.create(base + p))
                        .timeout(timeout)
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .header("Content-Type", "application/json");
            } else {
                // default to GET with query params
                String uri = String.format("%s%s?color=%s&state=%s", base, p, c, state);
                rb = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .timeout(timeout)
                        .GET();
            }

            if (!apiKeyHeader.isEmpty() && !apiKeyValue.isEmpty()) {
                rb.header(apiKeyHeader, apiKeyValue);
            }

            HttpRequest req = rb.build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            int code = resp.statusCode();
            return code >= 200 && code < 300;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }
}
