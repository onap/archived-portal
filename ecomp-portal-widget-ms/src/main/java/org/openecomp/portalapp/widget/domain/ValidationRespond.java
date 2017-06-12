package org.openecomp.portalapp.widget.domain;

public class ValidationRespond {
	private boolean valid;
	private String error;
	
	public ValidationRespond(boolean valid, String error){
		this.valid = valid;
		this.error = error;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ValidationRespond [valid=" + valid + ", error=" + error + "]";
	}
	
	
}
