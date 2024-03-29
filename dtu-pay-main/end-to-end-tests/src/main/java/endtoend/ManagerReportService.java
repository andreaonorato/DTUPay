package endtoend;

import classes.ManagerReport;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
public class ManagerReportService {
    Client client = ClientBuilder.newClient();
    WebTarget r = client.target("http://localhost:8080/");

    public ManagerReport getReport() {
        return r.path("report").request().get(ManagerReport.class);
    }
}
