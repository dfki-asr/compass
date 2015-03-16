/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.exception;

import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.jboss.logging.Logger;

@ExceptionHandler
public class SessionExpiredHandler {
	@Inject
	private Logger log;

	public void handleSessionExpired(@Handles ExceptionEvent<ViewExpiredException> exceptionEvent) {
		String viewId = exceptionEvent.getException().getViewId();
		log.warnv(exceptionEvent.getException(), "View \"{0}\" expired.", viewId);

		// the error page only fits the editor (relative links), so only handle it there.
		// to be fair though, if you manage to let your session expire in scenario management, your loss.
		if ("/editor/editor.xhtml".equals(viewId)) {
			renderErrorPage();
			exceptionEvent.handled();
		}
	}

	protected void renderErrorPage() {
		// modeled after http://stackoverflow.com/a/11206114
		FacesContext context = FacesContext.getCurrentInstance();
		ViewHandler viewFactory = context.getApplication().getViewHandler();
		UIViewRoot errorPage = viewFactory.createView(context, "/WEB-INF/error_pages/view_expired.xhtml");
		context.setViewRoot(errorPage);
		context.getPartialViewContext().setRenderAll(true);
		context.renderResponse();
	}
}
