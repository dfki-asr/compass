/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.exception;

public class CompassRuntimeException extends RuntimeException {

	public CompassRuntimeException() {
		super();
	}

	public CompassRuntimeException(final String message) {
		super(message);
	}

	public CompassRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CompassRuntimeException(final Throwable cause) {
		super(cause);
	}
}

