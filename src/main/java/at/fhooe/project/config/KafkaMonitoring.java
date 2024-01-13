package at.fhooe.project.config;

import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/health")
public class KafkaMonitoring {

    Set<String> names = new HashSet<>(Set.of(
            "outgoing-byte-total",
            "outgoing-byte-rate",
            "incoming-byte-total",
            "incoming-byte-rate",
            "network-io-total",
            "network-io-rate",
            "request-total",
            "request-rate",
            "response-total",
            "response-rate",
            "request-size-max",
            "request-size-avg",
            "record-size-max",
            "record-size-avg",
            "record-send-total",
            "record-send-rate"));
    private final KafkaStreams kafkaStreams;

    @Autowired
    public KafkaMonitoring(KafkaStreams kafkaStreams) {

        this.kafkaStreams = kafkaStreams;
    }

    @GetMapping
    public List<Map.Entry<String, String>> measure() {
        Map<MetricName, ? extends Metric> metrics = kafkaStreams.metrics();
        return metrics.entrySet().stream()
                .filter(metricNameEntry -> (metricNameEntry.getKey().group().equals("producer-metrics") ||
                        metricNameEntry.getKey().group().equals("consumer-metrics")) &&
                        names.contains(metricNameEntry.getKey().name())
                )
                .map(metricNameEntry -> Map.entry(metricNameEntry.getKey().toString(), metricNameEntry.getValue().metricValue().toString())).toList();
    }
}
