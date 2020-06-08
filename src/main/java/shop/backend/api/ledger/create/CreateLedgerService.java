package shop.backend.api.ledger.create;

import com.amazonaws.services.qldb.AmazonQLDB;
import com.amazonaws.services.qldb.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.backend.api.ledger.DescribeLedger;

@Slf4j
@AllArgsConstructor
@Service
public class CreateLedgerService {

    private AmazonQLDB qldbClient;

    private DescribeLedger describeLedger;

    public static final Long LEDGER_CREATION_POLL_PERIOD_MS = 10_000L;

    /**
     * Create a new ledger with the specified ledger name.
     *
     * @param ledgerName
     *              Name of the ledger to be created.
     * @return {@link CreateLedgerResult} from QLDB.
     */
    public CreateLedgerResult create(final String ledgerName) {
        log.info("Creating ledger with name: {}...", ledgerName);
        CreateLedgerResult result = qldbClient.createLedger(createLedgerRequest(ledgerName));
        waitForActive(ledgerName);
        return result;
    }

    private CreateLedgerRequest createLedgerRequest(String ledgerName) {
        return new CreateLedgerRequest()
                .withName(ledgerName)
                .withPermissionsMode(PermissionsMode.ALLOW_ALL)
                .withDeletionProtection(false);
    }

    /**
     * Wait for a newly created ledger to become active.
     *
     * @param ledgerName Name of the ledger to wait on.
     */
    public void waitForActive(final String ledgerName) {
        log.info("Waiting for ledger to become active...");
        while (true) {
            DescribeLedgerResult result = describeLedger.describe(ledgerName);
            if (result.getState().equals(LedgerState.ACTIVE.name())) {
                log.info("Success. Ledger is active and ready to use with STATE: {}", result.getState());
                log.info("Ledger description: {}", result);
                return;
            }
            log.info("The ledger is still creating. Please wait...");
            sleepFor10Seconds();
        }
    }

    private void sleepFor10Seconds() {
        try {
            Thread.sleep(LEDGER_CREATION_POLL_PERIOD_MS);
        } catch (InterruptedException e) {
            log.error("Interrupted thread exception, {}", e.getMessage());
        }
    }
}
