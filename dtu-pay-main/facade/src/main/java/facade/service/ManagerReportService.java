package facade.service;

import facade.classes.ManagerReport;
import facade.exceptions.ReportCreationException;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

import static utilities.EventTopics.MANAGER_REPORT_GENERATED;
import static utilities.EventTopics.MANAGER_REPORT_REQUESTED;

public class ManagerReportService {
    private final MessageQueue queue;

    private CompletableFuture<ManagerReport> managerReport;

    // Init handlers
    public ManagerReportService(MessageQueue mq) {
        queue = mq;
        this.queue.addHandler(MANAGER_REPORT_GENERATED, this::handleManagerReportGenerated);
    }

    // Function for report, publish event to PaymentService
    public ManagerReport getReport() throws ReportCreationException {
        managerReport = new CompletableFuture<>();
        Event event = new Event(MANAGER_REPORT_REQUESTED, new Object[]{});
        queue.publish(event);
        var result = managerReport.join();
        if (result.getManagerPayments().isEmpty())
            throw new ReportCreationException();
        return result;
    }

    // Handler for report, event comes from ReportMgmtService
    public void handleManagerReportGenerated(Event event) {
        ManagerReport mr = event.getArgument(0, ManagerReport.class);
        managerReport.complete(mr);
    }
}
