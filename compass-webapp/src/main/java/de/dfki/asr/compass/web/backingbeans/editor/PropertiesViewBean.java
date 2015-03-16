/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.web.backingbeans.CompassBean;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@ViewAccessScoped
@Named
public class PropertiesViewBean extends CompassBean implements Serializable {

	private static final long serialVersionUID = -830009908478489095L;

	@Inject
	protected ViewUpdaterBean viewUpdaterBean;

	public double getMinTransformAxisValue() {
		return -1.0E16;
	}

	public double getMaxTransformAxisValue() {
		return 1.0E16;
	}

	public String getTransformAxisValidatorMessage() {
		return getRangeValidatorMessage("validation.SceneNode.transformAxis.fieldName",
				getMinTransformAxisValue(), getMaxTransformAxisValue());
	}

	public double getMinScaleValue() {
		return 0.001;
	}

	public double getMaxScaleValue() {
		return 1.0E16;
	}

	public String getScaleValidatorMessage() {
		return getRangeValidatorMessage("validation.SceneNode.scale.fieldName",
				getMinScaleValue(), getMaxScaleValue());
	}

	public void refreshPropertiesView() {
		viewUpdaterBean.updatePropertyView();
	}
}
