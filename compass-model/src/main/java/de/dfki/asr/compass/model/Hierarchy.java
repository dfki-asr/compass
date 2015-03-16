/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

/** Interface for entities that are to be present in a hierarchy. This is a collection
 *  of methods needed e.g. in UITreeNode.
 *
 * @param <NodeType> the type of the inheriting class so we can apply nice typing of function signatures
 */
public interface Hierarchy<NodeType> {
	void setParent(NodeType parent);
	NodeType getParent();
	boolean isChildOf(NodeType other);
}
