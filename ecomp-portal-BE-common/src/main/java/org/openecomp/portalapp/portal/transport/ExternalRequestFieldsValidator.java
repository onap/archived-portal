package org.openecomp.portalapp.portal.transport;

public class ExternalRequestFieldsValidator {

	private boolean result;
	private String detailMessage;
	
	public ExternalRequestFieldsValidator(boolean result, String detailMessage) {
		super();
		this.result = result;
		this.detailMessage = detailMessage;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
	
	
	
}
