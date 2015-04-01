#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/sample")
@javax.ejb.Stateless
public class SampleRESTService {

	@GET
	@Path("/sampleREST")
	public Response getSampleModelList() {
		Response.status(Response.Status.NOT_FOUND).build();
	}
}
