# Prototex project
The Prototex project, using the Netty engine, aims to accelerate and simplify the development of network applications.

Prototex enables real-time, bidirectional and event-based communication.
It works on every platform or device, focusing equally on security, reliability and speed.


## Features

#### Simple and convenient API

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

#### Event-driven support
```java
client.on(NetworkEvent.CONNECTING, (session, input) -> /* … */);

client.on(NetworkEvent.DISCONNECTED, (session, input) -> /* … */);

client.on(NetworkEvent.CONNECTION_FAILED, (session, exception) -> /* … */);

client.on(NetworkEvent.CONNECTED, (session, input) -> 
    session.send(new ChatMessage("Client", "First message"))
           .addListener(future -> System.out.println("First message sent !"))
);
```

#### Native packet registry

By default, the program use **JSON** for communication.

You can define your own _**packet**_, and interface then by @PacketMapper.

_Note: @JsonMessage is not necessary because it is used by default_

```java
client.getPacketRegistry().register(ChatMessage.class);

@JsonMessage
@PacketMapper(id = 1)
class ChatMessage implements PacketInterface {

    @SerializedName("name")
    private final String name;

    @SerializedName("message")
    private final String message;

    @Override
    public void handle(PrototexSession session, Packet packet) {
        session.send(new ChatClient("John doe", "My response !"));
    }

}
```

Register an entire package
```java
client.getPacketRegistry().registerPackage("my.example.package");
```
