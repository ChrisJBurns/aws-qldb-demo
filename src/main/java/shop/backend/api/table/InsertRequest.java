package shop.backend.api.table;

import lombok.Data;

@Data
public class InsertRequest {
    private String ledgerName;
    private String tableName;
    private BodyData bodyData;
}
