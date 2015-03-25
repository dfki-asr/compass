/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named
public class DuplicateScenarioBean extends DialogCompassBean {

	private static final long serialVersionUID = -3805040932575827682L;

	@Inject
	protected Logger log;

	@Inject
	protected ScenarioManager manager;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	protected String duplicateScenarioName;

	public String getDuplicateScenarioName() {
		return duplicateScenarioName;
	}

	public void setDuplicateScenarioName(String name) {
		duplicateScenarioName = name;
	}

	@Override
	public void begin() {
		super.begin();
		showFacesMessageAtControl("validation.Scenario.name.Prompt", "duplicateScenarioForm:duplicateScenarioNameField", FacesMessage.SEVERITY_INFO);
		duplicateScenarioName = openScenarioBean.getSelectedScenario().getName();
	}

	@Override
	public String confirm() {
		try {
			manager.duplicateScenario(openScenarioBean.getSelectedScenario(), duplicateScenarioName);
		} catch (PersistenceException ex) {
			// The underlying exceptions aren't really specific enough (without resorting to parsing vendor-specific messages).
			// Even though other things may go wrong, I figure in 90% of the cases this will be the problem:
			showFacesMessageAtControl("A scenario of that name already exists", "duplicateScenarioForm:duplicateScenarioNameField", FacesMessage.SEVERITY_ERROR);
			return null;
		} catch (EntityConstraintException ex) {
			showFacesMessageAtControl(ex.getMessages().iterator().next(), "duplicateScenarioForm:duplicateScenarioNameField", FacesMessage.SEVERITY_ERROR);
			return null;
		}
		return getRedirectURL("/index.xhtml");
	}

	@Override
	public String cancel() {
		super.cancel();
		return getRedirectURL("/index.xhtml");
	}
}
