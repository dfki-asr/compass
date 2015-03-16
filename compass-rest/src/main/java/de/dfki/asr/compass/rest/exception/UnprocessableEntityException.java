/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exception;

public class UnprocessableEntityException extends Exception {

	private static final long serialVersionUID = -3015644273294432911L;

	public UnprocessableEntityException(final String message) {
		super(message);
	}

	public UnprocessableEntityException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnprocessableEntityException(final Throwable cause) {
		super(cause);
	}
}
