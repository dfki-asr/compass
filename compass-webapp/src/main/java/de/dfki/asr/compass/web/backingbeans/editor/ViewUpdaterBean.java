/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named
@RequestScoped
public class ViewUpdaterBean {

	private RequestContext primeFacesContext;

	public ViewUpdaterBean() {
		primeFacesContext = RequestContext.getCurrentInstance();
	}

	public void updateTreeView() {
		primeFacesContext.update("sceneTreeViewForm:sceneTreeView");
		primeFacesContext.update("sceneTreeViewForm:scene-hierarchy-buttons");
	}

	public void updatePropertyView() {
		primeFacesContext.update("propertyViewForm");
		primeFacesContext.update("sceneNodeComponentSelectorForm");
	}

	public void updatePrefabHierarchy() {
		primeFacesContext.update("prefabSetViewForm:prefabSetView");
	}

	public void updatePrefabSelection() {
		primeFacesContext.update("prefabSelectionGridForm");
	}

	public void scrollPrefabsToSavedPosition() {
		primeFacesContext.update("hiddenScrollBarFixingCallForPrefabSets");
	}

	public void prefabHierarchyChanged() {
		updatePrefabHierarchy();
		updatePrefabSelection();
		scrollPrefabsToSavedPosition();
	}

	public void scrollToSavedPosition() {
		primeFacesContext.update("hiddenScrollToSavedPositionCall");
	}

	public void sceneNodeHierarchyChanged() {
		updateTreeView();
		scrollToSavedPosition();
	}

	public void visibleSceneNodeSelected() {
		updateTreeView();
		updatePropertyView();
		scrollToSelected();
	}

	public void prefabSceneNodeSelected() {
		updateTreeView(); // since nothing in it is selected anymore
		updatePropertyView();
		scrollToSavedPosition();
	}

	public void sceneNodeSelectionCleared() {
		updateTreeView(); // since nothing in it is selected anymore
		updatePropertyView();
		scrollToSavedPosition();
	}

	public void scrollToSelected() {
		primeFacesContext.update("hiddenScrollBarScrollToSelectedCall");
	}
}
