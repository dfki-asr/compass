/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans;

import de.dfki.asr.compass.business.util.annotations.Hack;
import org.primefaces.model.TreeNode;

public final class PrimeFacesHelpers {

	private PrimeFacesHelpers(){
	}

	/*
	 Workaround for a bug in Primefaces where rowKeys aren't generated unless the root node's key is null.
	 RowKeys are necessary for the Tree to display properly so this function should be called any time the tree's
	 structure is changed.
	 */
	@Hack
	public static void forcePrimeFacesToRefreshTree(TreeNode rootNode) {
		rootNode.setRowKey(null);
	}
}
