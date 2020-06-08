package shop.backend.api.ledger.delete;

import com.amazonaws.services.qldb.AmazonQLDB;
import com.amazonaws.services.qldb.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.backend.api.ledger.DescribeLedger;

@Slf4j
@AllArgsConstructor
@Service
public class DeleteLedgerService {

    private AmazonQLDB qldbClient;

    private DescribeLedger describeLedger;

    public static final Long LEDGER_DELETION_POLL_PERIOD_MS = 20_000L;

    /**
     * Send a request to the QLDB database to delete the specified ledger.
     * Disables deletion protection before sending the deletion request.
     *
     * @param ledgerName
     *              Name of the ledger to be deleted.
     * @return DeleteLedgerResult.
     */
    public DeleteLedgerResult delete(final String ledgerName) {
        log.info("Attempting to delete the ledger with name: {}...", ledgerName);
        DeleteLedgerRequest request = new DeleteLedgerRequest().withName(ledgerName);
        DeleteLedgerResult result = qldbClient.deleteLedger(request);
        waitForDeleted(ledgerName);
        log.info("Success.");
        return result;
    }

    /**
     * Wait for the ledger to be deleted.
     *
     * @param ledgerName Name of the ledger being deleted.
     */
    public void waitForDeleted(final String ledgerName) {
        log.info("Waiting for the ledger to be deleted...");
        while (true) {
            try {
                describeLedger.describe(ledgerName);
                log.info("The ledger is still being deleted. Please wait...");
                sleepFor20Seconds();
            } catch (ResourceNotFoundException ex) {
                log.info("Success. The ledger is deleted.");
                break;
            }
        }
    }

    private void sleepFor20Seconds() {
        try {
            Thread.sleep(LEDGER_DELETION_POLL_PERIOD_MS);
        } catch (InterruptedException e) {
            log.error("Error sleeping for deletion of ledger: {}", e.getMessage());
        }
    }
}
