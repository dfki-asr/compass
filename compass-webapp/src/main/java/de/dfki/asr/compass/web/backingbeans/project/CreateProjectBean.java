/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
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
public class CreateProjectBean extends DialogCompassBean {

	private static final long serialVersionUID = -777269862795160683L;

	private String projectName;

	@Inject
	private ProjectManager manager;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public void begin() {
		showFacesMessageAtControl("Please enter a new project name.", "createProjectForm:ProjectNameInput", FacesMessage.SEVERITY_INFO);
	}

	@Override
	public String cancel() {
		return getRedirectURL("/index.xhtml");
	}

	@Override
	public String confirm() {
		try {
			manager.createNewProject(projectName);
		} catch (EntityConstraintException ex) {
			showFacesMessageAtControl(ex.getMessages().iterator().next(), "createProjectForm:ProjectNameInput", FacesMessage.SEVERITY_ERROR);
			return null;
		} catch (PersistenceException ex) {
			// The underlying exceptions aren't really specific enough (without resorting to parsing vendor-specific messages).
			// Even though other things may go wrong, I figure in 90% of the cases this will be the problem:
			showFacesMessageAtControl("A project with that name already exists.", "createProjectForm:ProjectNameInput", FacesMessage.SEVERITY_ERROR);
			return null;
		}
		return getRedirectURL("/index.xhtml");
	}

}
