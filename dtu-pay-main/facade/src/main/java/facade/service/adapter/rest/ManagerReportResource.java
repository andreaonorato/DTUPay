package facade.service.adapter.rest;

import facade.exceptions.ReportCreationException;
import facade.service.ManagerReportService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/report")
public class ManagerReportResource {
    ManagerReportService service = new ReportFactory().getService();

    @GET
    @Produces("application/json")
    public Response getReport() {
        try {
            var result = service.getReport();
            return Response.ok().entity(result).build();
        } catch (ReportCreationException e) {
            return Response.status(Response.Status.NO_CONTENT).entity(e.getMessage()).build();
        }
    }
}
