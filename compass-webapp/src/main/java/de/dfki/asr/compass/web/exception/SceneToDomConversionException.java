/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.exception;

public class SceneToDomConversionException extends Exception {

	private static final long serialVersionUID = -6795298235482768224L;

	public SceneToDomConversionException(String message) {
		super(message);
	}

	public SceneToDomConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SceneToDomConversionException(Throwable cause) {
		super(cause);
	}

}
