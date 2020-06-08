package shop.backend.api.table;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.backend.api.table.create.CreateTableService;
import shop.backend.api.table.delete.DeleteTableService;
import shop.backend.api.table.insert.InsertIntoTableService;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/table")
public class TableController {

    private final CreateTableService createTableService;
    private final DeleteTableService deleteTableService;
    private final InsertIntoTableService insertIntoTableService;


    @PostMapping("/create")
    public String createTable(@RequestBody TableRequest request) {
        log.info("Request to create Table named: \"{}\" in Ledger: {}", request.getTableName(), request.getLedgerName());
        createTableService.create(request.getTableName(), request.getLedgerName());
        return "Table created successfully";
    }

    @PostMapping("/delete")
    public String deleteTable(@RequestBody TableRequest request) {
        log.info("Request to create Table named: \"{}\" in Ledger: {}", request.getTableName(), request.getLedgerName());
        deleteTableService.delete(request.getTableName(), request.getLedgerName());
        return "Table deleted successfully";
    }

    @PostMapping("/insert")
    public String insertIntoTable(@RequestBody InsertRequest request) {
        log.info("Request to create Table named: \"{}\" in Ledger: {}", request.getTableName(), request.getLedgerName());
        insertIntoTableService.insert(request.getTableName(), request.getLedgerName(), request.getBodyData());
        return "Table deleted successfully";
    }

}
