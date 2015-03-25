/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.exception;

import javax.ejb.ApplicationException;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

@ApplicationException(inherited = true, rollback = true)
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = -8847001969400832698L;

	private static String processHibernateConstraintViolationExceptionMessage(final ConstraintViolationException ex) {
		String detail = ex.getSQLException().getMessage();
		String msg = "Could not process entity: Database constraint violation.";
		if (detail.contains("FOREIGN KEY")) {
			msg = "Could not process entity: One or more referenced entities do not exist.";
		} else if (detail.contains("UNIQUE KEY")) {
			msg = "Could not process entity: A duplicate name already exists, please choose another.";
		}
		return msg;
	}

	public PersistenceException(final HibernateException ex) {
		super(ex.getMessage(), ex);
	}

	public PersistenceException(final ConstraintViolationException ex) {
		super(processHibernateConstraintViolationExceptionMessage(ex), ex);
	}

	public PersistenceException(final javax.persistence.PersistenceException ex) {
		super(ex);
	}
}
