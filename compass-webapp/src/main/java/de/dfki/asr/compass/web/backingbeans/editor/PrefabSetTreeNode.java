/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.web.backingbeans.UITreeNode;
import de.dfki.asr.compass.model.PrefabSet;
import java.io.Serializable;

public class PrefabSetTreeNode extends UITreeNode<PrefabSet> implements Serializable {

	private static final long serialVersionUID = -1805898513588414597L;

	public PrefabSetTreeNode(PrefabSet prefabSet) {
		super(prefabSet);
	}

	@Override
	public String getType() {
		return "prefabset";
	}
}
