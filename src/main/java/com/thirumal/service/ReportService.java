package com.thirumal.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
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

	public ByteArrayOutputStream generateReport() {
	//	logger.debug("Generating report service is started");
		InputStream template = null;
		try {
			template = getTemplate("first");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//logger.debug("Template --> {}", template);
		System.out.println(template);
		return generate(template);
	}
	
	private ByteArrayOutputStream generate(InputStream rptDesign) {
		//logger.info("{}:{}", this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        ByteArrayOutputStream outStreamPDF = new ByteArrayOutputStream();
        EngineConfig config;// = new EngineConfig( );
       // config.setEngineHome( "put engine path here" );
       
        try{
        	 config = new EngineConfig();
        	    //delete the following line if using BIRT 3.7 (or later) POJO runtime
        	    //As of 3.7.2, BIRT now provides an OSGi and a POJO Runtime.

        	 config.setLogConfig("logs", Level.ALL);
        	    Platform.startup(config);
        	    //If using RE API in Eclipse/RCP application this is not needed.
        	    IReportEngineFactory factory = (IReportEngineFactory) Platform
        	            .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
        	    IReportEngine engine = factory.createReportEngine( config );
        	    engine.changeLogLevel( Level.WARNING );
        	
        	// Run reports, etc.
        	IReportRunnable design = engine.openReportDesign(rptDesign);
        	 RunAndRenderTask task = (RunAndRenderTask) engine.createRunAndRenderTask(design);
        	// task.getAppContext().put("org.eclipse.birt.report.data.oda.xml.closeInputStream", Boolean.TRUE);
        	// task.getAppContext().put("org.eclipse.birt.report.data.oda.xml.inputStream", xmlData);
        		//IRenderOption options = new RenderOption();     
//        		options.setOutputFormat("html");
//        		options.setOutputFileName("output/resample/eventorder.html");
        		PDFRenderOption pdfOptions = new PDFRenderOption( );
        		pdfOptions.setOutputStream(outStreamPDF);
        		pdfOptions.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
        		pdfOptions.setOutputFormat(IRenderOption.OUTPUT_FORMAT_PDF);
                task.setRenderOption(pdfOptions);
                task.run();
           //     logger.debug("Closed Successfully");
        	// destroy the engine.
        	
        	    engine.destroy();
        	    Platform.shutdown();
        	    //Bugzilla 351052
        	    RegistryProviderFactory.releaseDefault();
        	}catch ( BirtException e1 ){
        	    // Ignore
        	}
        System.out.println(outStreamPDF);
		return outStreamPDF;
	}
	
	public InputStream getTemplate(String rptDesign) throws IOException {
	//	logger.debug("The template requested is {}", rptDesign);
        InputStream file = null;
        try {
            file = new ClassPathResource("designs/templates/" + rptDesign + ".rptdesign").getInputStream();
           // logger.debug("is file available {}", file.available());
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String message = "Template: " + rptDesign + " is not found for application: ";
            //logger.error(message);
            throw new FileNotFoundException(message);
        } catch (IOException e) {
        	String message = "Template: " + rptDesign + "  is not found for application: " + " !";
           // logger.error(message);
            throw new IOException(message);
        }
    }

}
