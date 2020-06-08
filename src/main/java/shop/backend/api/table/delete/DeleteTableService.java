package shop.backend.api.table.delete;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.backend.api.table.ConnectToLedger;
import software.amazon.qldb.TransactionExecutor;

@Service
@Slf4j
public class DeleteTableService {

    public void delete(String tableName, String ledgerName) {
        ConnectToLedger.withName(ledgerName).getDriver().execute(txn -> {
            deleteTable(txn, tableName);
        }, (retryAttempt) -> log.info("Retrying due to OCC conflict..."));
    }

    public void deleteTable(final TransactionExecutor txn, final String tableName) {
        log.info("Deleting the '{}' table...", tableName);
        final String deleteTable = String.format("DROP TABLE %s", tableName);
        txn.execute(deleteTable);
        log.info("{} table deleted successfully.", tableName);
    }
}
