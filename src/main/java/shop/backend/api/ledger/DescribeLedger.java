package shop.backend.api.ledger;

import com.amazonaws.services.qldb.AmazonQLDB;
import com.amazonaws.services.qldb.model.DescribeLedgerRequest;
import com.amazonaws.services.qldb.model.DescribeLedgerResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class DescribeLedger {

    private AmazonQLDB qldbClient;

    /**
     * Describe a ledger.
     *
     * @param name
     *              Name of the ledger to describe.
     * @return {@link DescribeLedgerResult} from QLDB.
     */
    public DescribeLedgerResult describe(final String name) {
        DescribeLedgerRequest request = new DescribeLedgerRequest().withName(name);
        return qldbClient.describeLedger(request);
    }
}
