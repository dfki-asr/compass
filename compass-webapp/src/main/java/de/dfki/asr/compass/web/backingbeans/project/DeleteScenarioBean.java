/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class DeleteScenarioBean extends DialogCompassBean {

	private static final long serialVersionUID = -4781317662149541142L;

	@Inject
	protected Logger log;

	@Inject
	private ScenarioManager manager;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Override
	public String confirm() {
		try {
			manager.remove(openScenarioBean.getSelectedScenario());
		} catch (EntityNotFoundException ex) {
			// our entity has apparently already been deleted.
			log.warnv(ex, "Scenario id {0} has already vanished.", ex.getEntityId());
		}
		return super.confirm();
	}
}
