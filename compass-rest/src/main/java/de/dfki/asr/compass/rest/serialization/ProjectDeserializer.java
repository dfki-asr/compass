/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.Project;
import javax.inject.Inject;

public class ProjectDeserializer extends AbstractIDToEntityDeserializer<Project> {

	@Inject
	protected ProjectManager manager;

	@Override
	protected Project referenceById(final long id) throws EntityNotFoundException {
		return manager.referenceById(id);
	}

}
