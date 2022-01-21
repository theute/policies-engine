package com.redhat.cloud.policies.engine.clowder;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import java.util.Optional;

public class KafkaSaslInitializer {

    private static final Logger LOGGER = Logger.getLogger(KafkaSaslInitializer.class);
    private static final String KAFKA_SASL_JAAS_CONFIG = "kafka.sasl.jaas.config";
    private static final String KAFKA_SASL_MECHANISM = "kafka.sasl.mechanism";
    private static final String KAFKA_SECURITY_PROTOCOL = "kafka.security.protocol";
    private static final String KAFKA_SSL_TRUSTSTORE_LOCATION = "kafka.ssl.truststore.location";

    public static void init() {
        Config config = ConfigProvider.getConfig();
        Optional<String> kafkaSaslJaasConfig = config.getOptionalValue(KAFKA_SASL_JAAS_CONFIG, String.class);
        Optional<String> kafkaSaslMechanism = config.getOptionalValue(KAFKA_SASL_MECHANISM, String.class);
        Optional<String> kafkaSecurityProtocol = config.getOptionalValue(KAFKA_SECURITY_PROTOCOL, String.class);
        Optional<String> kafkaSslTruststoreLocation = config.getOptionalValue(KAFKA_SSL_TRUSTSTORE_LOCATION, String.class);

        if (kafkaSaslJaasConfig.isPresent() || kafkaSaslMechanism.isPresent() || kafkaSecurityProtocol.isPresent() || kafkaSslTruststoreLocation.isPresent()) {
            LOGGER.info("Initializing Kafka SASL configuration...");
            setValue(KAFKA_SASL_JAAS_CONFIG, kafkaSaslJaasConfig);
            setValue(KAFKA_SASL_MECHANISM, kafkaSaslMechanism);
            // TODO Temp, remove it ASAP
            kafkaSaslMechanism.ifPresent(value -> LOGGER.infof(KAFKA_SASL_MECHANISM + "=%s", value));
            setValue(KAFKA_SECURITY_PROTOCOL, kafkaSecurityProtocol);
            // TODO Temp, remove it ASAP
            kafkaSecurityProtocol.ifPresent(value -> LOGGER.infof(KAFKA_SECURITY_PROTOCOL + "=%s", value));
            setValue(KAFKA_SSL_TRUSTSTORE_LOCATION, kafkaSslTruststoreLocation);
            // TODO Temp, remove it ASAP
            kafkaSslTruststoreLocation.ifPresent(value -> LOGGER.infof(KAFKA_SSL_TRUSTSTORE_LOCATION + "=%s", value));
        }
    }

    private static void setValue(String configKey, Optional<String> configValue) {
        configValue.ifPresent(value -> {
            System.setProperty(configKey, value);
            LOGGER.info(configKey + " has been set");
        });
    }
}