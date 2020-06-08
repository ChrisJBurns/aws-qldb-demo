package shop.backend.api.table.create;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.backend.api.table.ConnectToLedger;
import software.amazon.qldb.TransactionExecutor;

@Service
@Slf4j
@AllArgsConstructor
public class CreateTableService {

    public void create(final String tableName, final String ledgerName) {
        ConnectToLedger.withName(ledgerName).getDriver().execute(txn -> {
            createTable(txn, tableName);
        }, (retryAttempt) -> log.info("Retrying due to OCC conflict..."));
    }

    public void createTable(final TransactionExecutor txn, final String tableName) {
        log.info("Creating the '{}' table...", tableName);
        final String createTable = String.format("CREATE TABLE %s", tableName);
        txn.execute(createTable);
        log.info("{} table created successfully.", tableName);
    }
}
