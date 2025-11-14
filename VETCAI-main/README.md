# ESP32 LED Control API Documentation

## Required Components

### Hardware Components
1. ESP32 Development Board (ESP32-WROOM-32 or similar)
2. 3 LEDs:
   - 1x Red LED
   - 1x Yellow LED
   - 1x Green LED
3. 3x 220Ω or 330Ω resistors (one for each LED)
4. Breadboard and jumper wires
5. Micro USB cable for ESP32 programming
6. Computer for development

### Software Requirements
1. Server-side:
   - Java JDK 17 or later
   - Maven 3.6+
   - Spring Boot (dependencies handled by Maven)

2. ESP32 Development:
   - Arduino IDE 2.0 or later
   - ESP32 board support package in Arduino IDE
   - Required Arduino libraries:
     - WiFi.h (built-in)
     - WebServer.h (built-in)

### Circuit Connection
1. Connect LEDs to ESP32 GPIO pins (with resistors in series):
   - Red LED: GPIO13
   - Yellow LED: GPIO12
   - Green LED: GPIO14
2. Connect LED cathodes (negative/shorter leg) to ESP32 ground (GND)

### Circuit Diagram
```
                                ESP32 DevKit
                               +------------+
                               |            |
     +----[220Ω]----RED LED---+  GPIO13    |
                               |            |
     +----[220Ω]--YELLOW LED--+  GPIO12    |
                               |            |
     +----[220Ω]--GREEN LED---+  GPIO14    |
                               |            |
     +-------------------------|  GND       |
                               |            |
                               +------------+

LED Connection Details:
┌──────────┬────────┬──────────┬─────────────┐
│   LED    │  GPIO  │ Resistor │    Notes    │
├──────────┼────────┼──────────┼─────────────┤
│   Red    │   13   │  220Ω    │ Long leg +  │
│  Yellow  │   12   │  220Ω    │ Long leg +  │
│  Green   │   14   │  220Ω    │ Long leg +  │
└──────────┴────────┴──────────┴─────────────┘

Notes:
- LED long leg (anode) → resistor → GPIO pin
- LED short leg (cathode) → GND
- All resistors are 220Ω (or 330Ω works too)
```

## Spring Boot REST Endpoints
Control LED lights on ESP32 devices through these REST endpoints:

```
POST /api/led/{color}/on  - Turn on a specific LED
POST /api/led/{color}/off - Turn off a specific LED

Where {color} is: red, yellow, or green

Response:
{
    "success": true
}

Error Response (HTTP 400 or 502):
{
    "success": false,
    "message": "Error details..."
}
```

## ESP32 Device API
The Spring Boot application communicates with ESP32 devices using configurable HTTP endpoints.

### Configuration (application.properties)
```properties
# Base URL of your ESP32 device
esp32.url=http://192.168.1.50

# HTTP method: GET or POST
esp32.method=GET

# API endpoint path (with or without leading slash)
esp32.path=/led

# Optional API key authentication
esp32.api-key-header=X-API-Key
esp32.api-key-value=your-secret-key

# Connection timeout
esp32.timeout-seconds=5
```

### Example ESP32 HTTP Formats

#### Using GET (Query Parameters)
```
GET http://192.168.1.50/led?color=red&state=on
GET http://192.168.1.50/led?color=yellow&state=off
```

#### Using POST (JSON Body)
```
POST http://192.168.1.50/led
Content-Type: application/json

{
    "color": "red",
    "state": "on"
}
```

### ESP32 Response Format
The ESP32 should return:
- Success: HTTP 200 OK
- Error: Any non-2xx status code

## Setup Instructions

1. Configure your ESP32's IP address and API format in `application.properties`
2. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Test an endpoint:
   ```bash
   curl -X POST http://localhost:8080/api/led/red/on
   ```

## Sample ESP32 Arduino Code

Basic ESP32 sketch that accepts GET requests:
```cpp
#include <WiFi.h>
#include <WebServer.h>

const char* ssid = "YourWiFiName";
const char* password = "YourWiFiPassword";

// LED pins
const int RED_PIN = 13;    // GPIO13
const int YELLOW_PIN = 12; // GPIO12
const int GREEN_PIN = 14;  // GPIO14

WebServer server(80);

void setup() {
  pinMode(RED_PIN, OUTPUT);
  pinMode(YELLOW_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }

  server.on("/led", HTTP_GET, handleLed);
  server.begin();
}

void handleLed() {
  String color = server.arg("color");
  String state = server.arg("state");
  
  int pin = -1;
  if (color == "red") pin = RED_PIN;
  else if (color == "yellow") pin = YELLOW_PIN;
  else if (color == "green") pin = GREEN_PIN;
  
  if (pin != -1) {
    digitalWrite(pin, state == "on" ? HIGH : LOW);
    server.send(200, "text/plain", "OK");
  } else {
    server.send(400, "text/plain", "Invalid color");
  }
}

void loop() {
  server.handleClient();
}
```
