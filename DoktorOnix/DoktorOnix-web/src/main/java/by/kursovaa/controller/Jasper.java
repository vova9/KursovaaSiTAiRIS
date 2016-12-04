/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Vladimir
 */
public class Jasper {

    public Jasper() {
    }

    JasperReport getCompiledFile(String file) throws JRException {
        File reportFile = new File(this.getClass().getClassLoader().getResource("report/" + file + ".jasper").getFile());
        if (!reportFile.exists()) {
            JasperCompileManager.compileReportToFile(this.getClass().getClassLoader().getResource("report/" + file + ".jrxml").getFile(),
                    this.getClass().getClassLoader().getResource("report/" + file + ".jasper").getFile());
        }
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
        return jasperReport;
    }

    <T1> void generateReportPDF(HttpServletResponse resp, Map parameters, JasperReport jasperReport,
            List<T1> kassa) throws JRException, NamingException, IOException {
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, new JRBeanCollectionDataSource(kassa));
        resp.reset();
        resp.resetBuffer();
        resp.setContentType("application/pdf");
        resp.setContentLength(bytes.length);
        ServletOutputStream ouputStream = resp.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();
    }
}
