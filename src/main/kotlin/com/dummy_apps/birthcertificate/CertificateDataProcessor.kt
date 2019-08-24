package com.dummy_apps.birthcertificate

import com.dummy_apps.birthcertificate.models.CertificateDocument
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import org.springframework.kafka.annotation.EnableKafkaStreams

@EnableKafkaStreams
@Configuration
class CertificateDataProcessor(@Value("\${topics.birth-certificate.input}") private val inputTopicName: String,
                               @Value("\${topics.birth-certificate.output}") private val outputTopicName: String){
    private val mapper = ObjectMapper()
    private val logger = LogManager.getLogger(CertificateDataProcessor::class.java)

    @Bean
    fun processor(streamBuilder: StreamsBuilder) : KStream<String, String> = streamBuilder
            .stream<String, String>(inputTopicName)
            .map(::mapToCertificateObject)
            .apply { to(outputTopicName) }

    /***
     * This method is for plain KAFKA STREAM usage
     */
    fun depricated_processor(){
        println("topic name is $inputTopicName")
        val streamsConfiguration = Properties()
        streamsConfiguration[StreamsConfig.APPLICATION_ID_CONFIG] = "birth-certificate"
        streamsConfiguration[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"

        streamsConfiguration[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        streamsConfiguration[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name

        val builder = StreamsBuilder()
        val textLines = builder.stream<String, String>(inputTopicName)

        val wordCounts = textLines
                .map(::mapToCertificateObject)
                .apply { to(outputTopicName) }
    }

    private fun mapToCertificateObject(key: String, value: String): KeyValue<String, String> =
            try {
                logger.info("mapping to certificate: $value")

                val certificateObj = mapper.readValue<CertificateDocument>(value)
                ReportGenerator().generate(certificateObj)
                KeyValue(certificateObj.email, certificateObj.grade)
            } catch (ex: Exception) {
                println("ERROR ${ex.message}")
                KeyValue("", "")
            }
}