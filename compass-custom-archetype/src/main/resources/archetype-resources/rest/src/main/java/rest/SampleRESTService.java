#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/sample")
public class SampleRESTService {

	@GET
	@Path("/sampleREST")
	public Response getSampleModelList() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
