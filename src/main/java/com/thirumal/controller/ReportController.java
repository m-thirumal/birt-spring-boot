/**
 * 
 */
package com.thirumal.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String generateReport() {
		return reportService.generateReport();
	}
}
