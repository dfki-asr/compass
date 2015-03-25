/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.model.PrefabSet;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class PrefabSetRenameBean {

	private static final long serialVersionUID = 2938597299067666002L;
	private PrefabSet editedPrefabSet;
	private ScenarioEditorBean scenarioEditorBean;

	public PrefabSet getEditedPrefabSet() {
		return editedPrefabSet;
	}

	public PrefabSetRenameBean() {}

	@Inject
	public PrefabSetRenameBean(ScenarioEditorBean scenarioEditorBean) {
		this.scenarioEditorBean = scenarioEditorBean;
		editedPrefabSet = new PrefabSet();
		editedPrefabSet.setName(scenarioEditorBean.getSelectedPrefabSet().getName());
	}

	public void saveChanges() {
		PrefabSet toChange = scenarioEditorBean.getSelectedPrefabSet();
		toChange.setName(editedPrefabSet.getName());
		scenarioEditorBean.saveSelectedPrefabSetChanges();
	}
}
