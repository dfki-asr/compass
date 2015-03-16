/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EditProjectBean extends DialogCompassBean {

	private static final long serialVersionUID = 2938597299067666002L;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Inject
	private ProjectManager manager;

	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String name) {
		projectName = name;
	}

	@Override
	public void begin() {
		projectName = openScenarioBean.getSelectedProject().getName();
	}

	@Override
	public String confirm() {
		try {
			manager.setProjectName(openScenarioBean.getSelectedProject(), projectName);
		} catch (PersistenceException ex) {
			// The underlying exceptions aren't really specific enough (without resorting to parsing vendor-specific messages).
			// Even though other things may go wrong, I figure in 90% of the cases this will be the problem:
			showFacesMessageAtControl("A project of this name already exists.", "editProjectForm:editProjectNameField", FacesMessage.SEVERITY_ERROR);
			return null;
		} catch (EntityConstraintException ex) {
			showFacesMessageAtControl(ex.getMessages().iterator().next(), "editProjectForm:editProjectNameField", FacesMessage.SEVERITY_ERROR);
			return null;
		}
		return getRedirectURL("/index.xhtml");
	}

	@Override
	public String cancel() {
		return getRedirectURL("/index.xhtml");
	}
}
