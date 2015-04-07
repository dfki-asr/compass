#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * An additional REST service.
 * You can, of course, extend COMPASS' REST API by adding more JAX-RS classes,
 * such as this.
 * You can also add functionality (to a certain extent) to the existing API,
 * by specifying identical @Path annotations to something that is already in
 * COMPASS' core REST classes. You are limited by JAX-RS' method selection
 * algorithm however, which only looks for @Path, Method Type (@GET) and
 * Content-Type (@Consumes, @Produces) annotations. See the JAX-RS standard for
 * details.
 * Consider providing a REST API for your Business Logic methods.
 */
@Path("/sample")
@javax.ejb.Stateless
public class SampleRESTService {

	@GET
	public Response getSampleModelList() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
