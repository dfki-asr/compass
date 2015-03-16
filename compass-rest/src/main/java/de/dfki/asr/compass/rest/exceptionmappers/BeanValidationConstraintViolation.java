/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.metadata.ConstraintDescriptor;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BeanValidationConstraintViolation implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(final ConstraintViolationException exception) {
		StringBuilder responseMessage = new StringBuilder();
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			responseMessage.append(getViolationMessage(violation));
			responseMessage.append(System.getProperty("line.separator"));
		}
		return Response.status(UnprocessableEntity.STATUS_UNPROCESSABLE_ENTITY)
			.entity(responseMessage.toString())
			.build();
	}

	/** Return violation.getMessage() for all constraints, but a NotNull constraint.
	 *  Here the method won't include the field, whose constraint is violated, but
	 *  simply "may not be null". This method will prepend the field name in these
	 *  cases.
	 *
	 * @param violation
	 * @return the detailed error message to be included in the response body
	 */
	private String getViolationMessage(final ConstraintViolation<?> violation) {
		ConstraintDescriptor desc = violation.getConstraintDescriptor();
		if (desc.getAnnotation().annotationType() != javax.validation.constraints.NotNull.class) {
			return violation.getMessage();
		}
		String violatedPropertyName = getViolatedPropertyName(violation);
		return violatedPropertyName + " " + violation.getMessage();
	}

	private String getViolatedPropertyName(final ConstraintViolation<?> violation) {
		Path propertyPath = violation.getPropertyPath();
		String fieldName = "";
		// get field name of leaf node in path (i.e. last one in iteration)
		for (Node node : propertyPath) {
			fieldName = node.getName();
		}
		return fieldName;
	}

}
