/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.events;

/**
 * Base class for compass events related to database updates. Would be nice to make a generic out of
 * it, but Weld doesn't support events with generic arguments.
 */
public class CompassDatabaseEvent {

	public Object target;

	protected CompassDatabaseEvent(final Object target) {
		this.target = target;
	}
}
