package br.com.inatel.projetoFinal;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Path("/entrega")
public class EntregaJersey {
	@Context
	private UriInfo uriInfo;
	
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response criaEntrega(Entrega entrega) throws SQLException {
		EntregaDAO bd = new EntregaDAO();
		bd.criaEntrega(entrega);
		GenericEntity entity = new GenericEntity<Entrega>(entrega){};
		
		try {
			return Response.created(new URI(String.format("%s/%s", uriInfo.getAbsolutePath(), entrega.getNumeroPedido()))).entity(entity).build();
		} catch (URISyntaxException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("{numeroPedido}")
	public Response getPedido(@PathParam ("numeroPedido") int numeroPedido) throws SQLException, URISyntaxException {
		EntregaDAO bd = new EntregaDAO();
		Entrega e = bd.buscaEntrega(numeroPedido);
		if(e == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		GenericEntity entity = new GenericEntity<Entrega>(e){};
		
		return Response.status(Status.OK).entity(entity).build();
	}
}
