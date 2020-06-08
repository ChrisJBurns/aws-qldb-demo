package shop.backend.api.ledger;

import com.amazonaws.services.qldb.model.CreateLedgerResult;
import com.amazonaws.services.qldb.model.DeleteLedgerResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.backend.api.ledger.create.CreateLedgerService;
import shop.backend.api.ledger.delete.DeleteLedgerService;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/ledger")
public class LedgerController {

    private final CreateLedgerService createLedgerService;
    private final DeleteLedgerService deleteLedgerService;

    @PostMapping("/create")
    public CreateLedgerResult createLedger(@RequestBody LedgerRequest request) {
        log.info("Request to create Ledger named: \"{}\" received", request.getLedgerName());
        return createLedgerService.create(request.getLedgerName());
    }

    @PostMapping("/delete")
    public DeleteLedgerResult deleteLedger(@RequestBody LedgerRequest request) {
        log.info("Request to delete Ledger named: \"{}\" received", request.getLedgerName());
        return deleteLedgerService.delete(request.getLedgerName());
    }
}
