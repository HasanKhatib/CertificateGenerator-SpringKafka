package com.dummy_apps.birthcertificate

import com.dummy_apps.birthcertificate.models.CertificateDocument
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.streams.KeyValue
import org.apache.logging.log4j.LogManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthCertificateApplication

private val logger = LogManager.getLogger(CertificateDataProcessor::class.java)
private val mapper = ObjectMapper()

fun main(args: Array<String>) {
    runApplication<BirthCertificateApplication>(*args)
}


