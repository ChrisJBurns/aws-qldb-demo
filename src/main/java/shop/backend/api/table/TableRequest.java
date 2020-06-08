package shop.backend.api.table;

import lombok.Data;

@Data
public class TableRequest {

    private String ledgerName;
    private String tableName;
}
