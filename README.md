# Prototex project
The Prototex project, using the Netty engine, aims to accelerate and simplify the development of network applications.

Prototex enables real-time, bidirectional and event-based communication.
It works on every platform or device, focusing equally on security, reliability and speed.


## Features

#### Simple and convenient API

Sample code:

Client side:
```java
PrototexClient client = new PrototexClient(PrototexConfiguration.builder().port(6999).build());
client.connect();
```

Server side:
```java
PrototexServer server = new PrototexServer(PrototexConfiguration.builder().port(6999).build());
server.bind();
```

#### Auto-reconnection support

A disconnected client can try to reconnect forever, until the server is available again.

```java
PrototexConfiguration.builder().port(6999).autoReconnect(true).build();
```
