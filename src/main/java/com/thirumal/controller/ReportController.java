/**
 * 
 */
package com.thirumal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/generate-report")
	public ResponseEntity<ByteArrayResource> generateReport() {
		var byteArrayOutputStream = reportService.generateReport();
		byte[] bytes =  byteArrayOutputStream.toByteArray();
		return ResponseEntity.ok().contentLength(bytes.length).contentType(MediaType.APPLICATION_PDF)
        .header("Content-Disposition", "attachment; filename=output.pdf").body(new ByteArrayResource(bytes));
	}
}
