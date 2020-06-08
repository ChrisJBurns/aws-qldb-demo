package shop.backend.api.table.insert;

import com.amazon.ion.IonValue;
import com.amazon.ion.system.IonSystemBuilder;
import com.fasterxml.jackson.dataformat.ion.IonObjectMapper;
import com.fasterxml.jackson.dataformat.ion.ionvalue.IonValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shop.backend.api.table.BodyData;
import shop.backend.api.table.ConnectToLedger;
import software.amazon.qldb.TransactionExecutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class InsertIntoTableService {

    public static final IonObjectMapper MAPPER = new IonValueMapper(IonSystemBuilder.standard().build());

    public void insert(final String tableName, final String ledgerName, BodyData data) {
        ConnectToLedger.withName(ledgerName).getDriver().execute(txn -> {
            insertDocuments(txn, tableName, Collections.unmodifiableList(Collections.singletonList(data)));
        }, (retryAttempt) -> log.info("Retrying due to OCC conflict..."));
        log.info("Documents inserted successfully!");
    }

    /**
     * Insert the given list of documents into the specified table and return the document IDs of the inserted documents.
     *
     * @param txn
     *              The {@link TransactionExecutor} for lambda execute.
     * @param tableName
     *              Name of the table to insert documents into.
     * @return a list of document IDs.
     * @throws IllegalStateException if failed to convert documents into an {@link IonValue}.
     */
    public static void insertDocuments(final TransactionExecutor txn, final String tableName,
                                               final List<BodyData>documents) {
        log.info("Inserting some documents in the {} table...", tableName);
        try {
            final String statement = String.format("INSERT INTO %s ?", tableName);
            final IonValue ionDocuments = MAPPER.writeValueAsIonValue(documents);
            final List<IonValue> parameters = Collections.singletonList(ionDocuments);
            txn.execute(statement, parameters);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }
}
