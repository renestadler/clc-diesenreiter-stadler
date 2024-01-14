package at.fhooe.project.config;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamsHealthIndicator implements HealthIndicator {

    @Autowired
    private KafkaStreams kafkaStreams;

    @Override
    public Health health() {
        KafkaStreams.State kafkaStreamsState = kafkaStreams.state();

        // CREATED, RUNNING or REBALANCING
        if (kafkaStreamsState == KafkaStreams.State.CREATED || kafkaStreamsState.isRunningOrRebalancing()) {
            //set details if you need one
            return Health.up().build();
        }

        // ERROR, NOT_RUNNING, PENDING_SHUTDOWN,
        return Health.down().withDetail("state", kafkaStreamsState.name()).build();
    }
}