# Kafka cheat sheet

## Mirror maker

### Useful links

[KIP-382: MirrorMaker 2.0 - Apache Kafka - Apache Software Foundation](https://cwiki.apache.org/confluence/display/KAFKA/KIP-382%3A+MirrorMaker+2.0#KIP382:MirrorMaker2.0-InternalTopics)
[https://cwiki.apache.org/confluence/display/KAFKA/KIP-597%3A+MirrorMaker2+internal+topics+Formatters](https://cwiki.apache.org/confluence/display/KAFKA/KIP-597%3A+MirrorMaker2+internal+topics+Formatters)

### Consuming from internal topics:

The below only works on kafka 2.7.0 or newer:

HeartbeatFormatter: `> ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic heartbeats --formatter org.apache.kafka.connect.mirror.formatters.HeartbeatFormatter --from-beginning`
<span class="colour" style="color: rgb(23, 43, 77);">`Heartbeat{sourceClusterAlias=B, targetClusterAlias=A, timestamp=1588502119726}`</span>

CheckpointFormatter:
<span class="colour" style="color: rgb(23, 43, 77);">   `> ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic A.checkpoints.internal --formatter org.apache.kafka.connect.mirror.formatters.CheckpointFormatter --from-beginning`</span>

<span class="colour" style="color: rgb(23, 43, 77);">`Checkpoint{consumerGroupId=qwert, topicPartition=A.heartbeat, upstreamOffset=631, downstreamOffset=631, metatadata=}`</span>

OffsetSyncFormatter:
<span class="colour" style="color: rgb(23, 43, 77);">`  > ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic mm2-offset-syncs.B.internal --formatter org.apache.kafka.connect.mirror.formatters.OffsetSyncFormatter --from-beginning`</span>

<span class="colour" style="color: rgb(23, 43, 77);">`OffsetSync{topicPartition=heartbeat-0, upstreamOffset=0, downstreamOffset=0}`</span>
<br>
### FAQ:

*What is the mirrormaker2 default offset start when mirroring a new topic?*
<span class="colour" style="color: var(--color-prettylights-syntax-string);">auto.offset.reset = earliest is the </span>[default](https://github.com/apache/kafka/blob/d63eaaaa0181bb7b9b4f5ed088abc00d7b32aeb0/connect/mirror/src/main/java/org/apache/kafka/connect/mirror/MirrorConnectorConfig.java#L233) <span class="colour" style="color: var(--color-prettylights-syntax-string);">setting.</span><span class="colour" style="color: rgb(36, 41, 46);">)</span>