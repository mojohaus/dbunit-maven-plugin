package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal insert
 */
public class InsertMojo extends AbstractDbUnitMojo {
    DatabaseOperation getOperation() {
        return DatabaseOperation.INSERT;
    }
}
