package com.dummy_apps.birthcertificate

import com.dummy_apps.birthcertificate.models.CertificateDocument
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.design.JRDesignQuery
import net.sf.jasperreports.engine.design.JasperDesign
import net.sf.jasperreports.engine.xml.JRXmlLoader
import org.springframework.util.ResourceUtils
import sun.security.ec.point.ProjectivePoint
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import org.apache.logging.log4j.LogManager
import net.sf.jasperreports.engine.export.JRPdfExporter
import java.io.File
import java.io.FileOutputStream
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource








class ReportGenerator {
    private val logger = LogManager.getLogger(ReportGenerator::class.java)

    fun generate(bc: CertificateDocument) {
        val rValues = mutableListOf<CertificateDocument>(bc)
        var rMapValues = HashMap<String,String>()
        val beanColDataSource = JRBeanCollectionDataSource(rValues)

        val jrxmlInput = ResourceUtils.getFile("classpath:BC_A4.jrxml").inputStream()
        val design: JasperDesign = JRXmlLoader.load(jrxmlInput)

        val jasperReport = JasperCompileManager.compileReport(design)

        val jasperPrint = JasperFillManager.fillReport(jasperReport,rMapValues, beanColDataSource)
//        JasperExportManager.exportReportToPdfFile(jasperPrint,
//                "${bc.name}-manager.pdf");
        val pdfExporter = JRPdfExporter()
        pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
        pdfExporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jasperPrint)
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "${bc.name}-exporter.pdf")

        pdfExporter.exportReport()

        logger.info("Report generated for ${bc.email}")
    }

}