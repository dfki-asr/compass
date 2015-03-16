/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@ViewAccessScoped
public abstract class DialogCompassBean extends CompassBean implements Serializable {

	@PostConstruct
	public void begin() {
	}

	public String cancel() {
		return "";
	}

	public String confirm() {
		return "";
	}
}
