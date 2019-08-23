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
    mapToCertificateObject("{\"name\": \"Hasan Alkhatib\",\"age\": \"27\",\"email\": \"hasan@gmail.com\",\"grade\": \"102\"}")
    logger.info("Application Started!")
}


private fun mapToCertificateObject(json: String) = try {
    logger.info("mapping to certificate: $json")

    val certificateObj = mapper.readValue<CertificateDocument>(json)
    ReportGenerator().generate(certificateObj)
    KeyValue(certificateObj.email, certificateObj.grade)
} catch (ex: Exception) {
    println("ERROR ${ex.message}")
    KeyValue("", "")
}
