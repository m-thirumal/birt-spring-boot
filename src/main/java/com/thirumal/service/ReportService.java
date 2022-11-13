package com.thirumal.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Thirumal
 *
 */
@Service
public class ReportService {

	public ByteArrayOutputStream generateReport(InputStream xml) throws Exception {
		// logger.debug("Generating report service is started");
		InputStream template = null;
		try {
			template = getTemplate("first");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// logger.debug("Template --> {}", template);
		System.out.println(template);
		return generate(template, xml);
	}

	private ByteArrayOutputStream generate(InputStream rptDesign, InputStream xml) throws Exception {
		// logger.info("{}:{}", this.getClass().getSimpleName(),
		// Thread.currentThread().getStackTrace()[1].getMethodName());
		ByteArrayOutputStream outStreamPDF = new ByteArrayOutputStream();
		EngineConfig config;// = new EngineConfig( );
		// config.setEngineHome( "put engine path here" );
		try {
			config = new EngineConfig();
			// delete the following line if using BIRT 3.7 (or later) POJO runtime
			// As of 3.7.2, BIRT now provides an OSGi and a POJO Runtime.
			config.setLogConfig("logs", Level.ALL);
			Platform.startup(config);
			// If using RE API in Eclipse/RCP application this is not needed.
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			IReportEngine engine = factory.createReportEngine(config);
			engine.changeLogLevel(Level.ALL);

			// Run reports, etc.
			// Create task to run the report - use the task to execute the report and save
			// to disk.
			IReportRunnable design = engine.openReportDesign(rptDesign);
			RunAndRenderTask task = (RunAndRenderTask) engine.createRunAndRenderTask(design);
			task.getAppContext().put("org.eclipse.datatools.enablement.oda.xml.inputStream", xml);
			task.getAppContext().put("org.eclipse.datatools.enablement.oda.xml.closeInputStream", Boolean.TRUE);
			PDFRenderOption pdfOptions = new PDFRenderOption();
			pdfOptions.setOutputStream(outStreamPDF);
			pdfOptions.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
			pdfOptions.closeOutputStreamOnExit(true);
			pdfOptions.setOutputFormat(IRenderOption.OUTPUT_FORMAT_PDF);
			task.setRenderOption(pdfOptions);
			task.run();
			task.close();
			engine.destroy();
			// Bugzilla 351052
			// RegistryProviderFactory.releaseDefault();
		} catch (EngineException e) {
            e.printStackTrace();
            throw new Exception("Engine exception");
        } catch (BirtException e) {
            e.printStackTrace();
            throw new Exception("Not able to generate report");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to generate report");
        } 
        finally {
            if (rptDesign != null) {
                closeInputStream(rptDesign, "rptDesign inputstream");
            }
            if (xml != null) {
                closeInputStream(xml, "XML inputstream");
            }
            //engine.destroy();
            Platform.shutdown();
            RegistryProviderFactory.releaseDefault();
        }
        return outStreamPDF;
	}
	
   private void closeInputStream(InputStream inputStream, String type) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public InputStream getTemplate(String rptDesign) throws IOException {
		// logger.debug("The template requested is {}", rptDesign);
		InputStream file = null;
		try {
			file = new ClassPathResource("designs/templates/" + rptDesign + ".rptdesign").getInputStream();
			// logger.debug("is file available {}", file.available());
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			String message = "Template: " + rptDesign + " is not found for application: ";
			// logger.error(message);
			throw new FileNotFoundException(message);
		} catch (IOException e) {
			String message = "Template: " + rptDesign + "  is not found for application: " + " !";
			// logger.error(message);
			throw new IOException(message);
		}
	}

}
