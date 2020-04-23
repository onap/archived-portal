package org.onap.portalapp.widget.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping(value = { "/health" }, produces = "application/json")
	public HealthStatus getWidgetCatalog(HttpServletRequest request, HttpServletResponse response) {
		return new HealthStatus("ok");
	}
	
	class HealthStatus {
		private String status;
		
		public HealthStatus(String status){
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		
	}
}