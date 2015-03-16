/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.web.backingbeans.UITreeNode;
import de.dfki.asr.compass.model.SceneNode;
import java.io.Serializable;

public class SceneNodeTreeNode extends UITreeNode<SceneNode> implements Serializable {

	private static final long serialVersionUID = 888159566793493181L;

	public SceneNodeTreeNode(SceneNode sceneNode) {
		super(sceneNode);
	}

	@Override
	public String getType() {
		return "scenenode";
	}
}
