package facade.service.adapter.rest;

import facade.classes.Merchant;
import facade.classes.Payment;
import facade.exceptions.MerchantServiceException;
import facade.exceptions.ReportCreationException;
import facade.service.MerchantService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchant")
public class MerchantResource {
    MerchantService service = new MerchantFactory().getService();

    @Path("report")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces("application/json")
    public Response getReport(@QueryParam("id") String id) {
        try {
            var result = service.getReport(id);
            return Response.ok().entity(result).build();
        } catch (ReportCreationException e) {
            return Response.status(Response.Status.NO_CONTENT).entity(e.getMessage()).build();
        }
    }

    // Merchant payment - requires a payment with a token merchant ID and amount
    @Path("payment")
    @POST
    @Consumes("application/json")
    @Produces(MediaType.TEXT_PLAIN)
    public Response pay(Payment payment) {
        try {
            return Response.ok().entity(service.pay(payment)).build();
        } catch (MerchantServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Register merchant
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(Merchant merchant) {

        try {
            return Response.ok().entity(service.register(merchant)).build();
        } catch (MerchantServiceException e) { // Return error if merchant already exists
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // De-register merchant
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response deregister(@QueryParam("id") String id) {

        try {
            return Response.ok().entity(service.deregister(id)).build();
        } catch (MerchantServiceException e) { // Return error if merchant is unknown
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
