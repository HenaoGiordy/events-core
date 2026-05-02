# events-core

Reusable CQRS event modeling library for Java 17+.

Provides the base building blocks for command/event-driven architectures.
The transport layer (Kafka, SQS, RabbitMQ) is intentionally left to the consuming project.

## What's included

- `Command` / `CommandHandler` — command contracts
- `Event` / `EventHandler` / `BaseEvent` — event contracts
- `CommandMessage` / `EventMessage` — broker message structures
- `AggregateRoot` — base class that accumulates and emits domain events
- `HandlerRegistry` — thread-safe central registry that dispatches commands and events by name

## Installation (JitPack)

**build.gradle:**
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.TU_USUARIO:events-core:1.0.0'
}
```

## Usage

### 1. Implement your command and handler

```java
public class CreateOrderCommand implements Command {
    private final String customerId;
    private final BigDecimal amount;
    // constructor + getters...

    @Override
    public String getCommandName() { return "CreateOrderCommand"; }
}

public class CreateOrderHandler implements CommandHandler<CreateOrderCommand> {

    @Override
    public void handle(CreateOrderCommand command) {
        Order order = new Order();
        order.create(command.getCustomerId(), command.getAmount());

        // publish events to your broker
        order.pullEvents().forEach(eventProducer::publish);
    }

    @Override
    public Class<CreateOrderCommand> getCommandType() {
        return CreateOrderCommand.class;
    }
}
```

### 2. Register handlers at startup

```java
HandlerRegistry registry = new HandlerRegistry();
registry.registerCommand(new CreateOrderHandler(eventProducer));
registry.registerEvent(new SendEmailHandler());
registry.registerEvent(new UpdateInventoryHandler());
```

### 3. Dispatch from your broker consumer

```java
// Kafka / SQS / RabbitMQ consumer — only this changes per broker
CommandMessage message = objectMapper.readValue(rawJson, CommandMessage.class);
registry.dispatchCommand(message.commandName(), message);

EventMessage eventMessage = objectMapper.readValue(rawJson, EventMessage.class);
registry.dispatchEvent(eventMessage.eventName(), eventMessage);
```

## Publishing a new version

```bash
git tag 1.1.0
git push origin 1.1.0
```

Then visit [jitpack.io](https://jitpack.io) and trigger a build for the new tag.
