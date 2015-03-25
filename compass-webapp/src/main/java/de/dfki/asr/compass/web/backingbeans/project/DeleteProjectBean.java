/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import de.dfki.asr.compass.model.Project;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class DeleteProjectBean extends DialogCompassBean {

	private static final long serialVersionUID = 5332095035033218467L;

	@Inject
	protected Logger log;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Inject
	protected ProjectManager manager;

	@Override
	public String confirm() {
		Project selectedProject = openScenarioBean.getSelectedProject();
		try {
			manager.remove(selectedProject);
		} catch (EntityNotFoundException ex) {
			log.errorv(ex, "Selected Project not found. Maybe someone removed it under our noses already?");
			// at any rate, it should be gone. *shrug*
		}
		return "";
	}

	@Override
	public String cancel() {
		return "";
	}
}
