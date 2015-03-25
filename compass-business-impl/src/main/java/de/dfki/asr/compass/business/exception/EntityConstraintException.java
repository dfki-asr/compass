/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.exception;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.ApplicationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ApplicationException(inherited = true, rollback = true)
public class EntityConstraintException extends RuntimeException {

	private final List<String> messages = new ArrayList<>();

	public EntityConstraintException(final String message) {
		super(message);
		messages.add(message);
	}

	private static String buildMessage(final ConstraintViolationException ex) {
		StringBuilder message = new StringBuilder(32);
		message.append("Invalid values: ");
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			message.append(violation.getRootBeanClass().getSimpleName());
			message.append('.');
			message.append(violation.getPropertyPath());
			message.append(' ');
			message.append(violation.getMessage());
			message.append("; ");
		}
		return message.toString();
	}

	public EntityConstraintException(final ConstraintViolationException ex) {
		super(buildMessage(ex), ex);
		saveMessages(ex);
	}

	private void saveMessages(final ConstraintViolationException ex) {
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			messages.add(violation.getMessage());
		}
	}

	public List<String> getMessages() {
		return messages;
	}

}
