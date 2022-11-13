/**
 * 
 */
package com.thirumal.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.thirumal.service.ReportService;

/**
 * @author Thirumal
 *
 */
@RestController
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@PostMapping(value="/generate-report", consumes = {"multipart/form-data"})
	public ResponseEntity<ByteArrayResource> generateReport(@RequestPart(value = "xml", required = true) String xml) throws Exception {
		var byteArrayOutputStream = reportService.generateReport(new ByteArrayInputStream(xml.getBytes()));
		byte[] bytes =  byteArrayOutputStream.toByteArray();
		return ResponseEntity.ok().contentLength(bytes.length).contentType(MediaType.APPLICATION_PDF)
        .header("Content-Disposition", "attachment; filename=output.pdf").body(new ByteArrayResource(bytes));
	}
}
