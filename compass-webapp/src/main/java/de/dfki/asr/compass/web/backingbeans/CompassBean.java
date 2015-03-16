/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.logging.Logger;

public abstract class CompassBean {

	@Inject
	private Logger log;

	private ResourceBundle messages;
	private ResourceBundle validationMessages;
	private ResourceBundle errorMessages;

	public CompassBean() {
		messages = ResourceBundle.getBundle("messages");
		validationMessages = ResourceBundle.getBundle("validationMessages");
		errorMessages = ResourceBundle.getBundle("errorMessages");
	}

	protected void showFacesMessageAtControl(String messageKey, String uiControlID, FacesMessage.Severity severity) {
		String messageText = getMessageForKey(messageKey);
		FacesMessage facesMessage = new FacesMessage(messageText);
		facesMessage.setSeverity(severity);
		FacesContext.getCurrentInstance().addMessage(uiControlID, facesMessage);
	}

	protected void showFacesMessageAtMessageTag(String messageKey, FacesMessage.Severity severity) {
		this.showFacesMessageAtControl(messageKey, null, severity);
	}

	/**
	 * @param page
	 * @return the URL that should be handed back to JSF so that it performs the redirect
	 *
	 * See {
	 * @linktourl http://stackoverflow.com/questions/4639205/primefaces-commandbutton-doesnt-navigate-or-update}
	 */
	protected String getRedirectURL(String page) {
		return page + "?faces-redirect=true";
	}

	protected String getMessageForKey(String messageKey) {
		if (messages.containsKey(messageKey)) {
			return messages.getString(messageKey);
		} else if (validationMessages.containsKey(messageKey)) {
			return validationMessages.getString(messageKey);
		} else if (errorMessages.containsKey(messageKey)) {
			return errorMessages.getString(messageKey);
		}
		log.warnv("Message Key \"{0}\" not found.", messageKey);
		return messageKey;
	}

	protected String getRangeValidatorMessage(String fieldNameKey, double minValue, double maxValue) {
		return getMessageForKey(fieldNameKey)
			+ " " + getMessageForKey("validation.operators.invalidRangeBegin")
			+ " " + minValue
			+ " " + getMessageForKey("validation.operators.and")
			+ " " + maxValue + ".";
	}
}
