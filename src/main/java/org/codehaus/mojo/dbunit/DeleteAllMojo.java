package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal delete-all
 */
public class DeleteAllMojo extends AbstractDbUnitMojo {
    DatabaseOperation getOperation() {
        return DatabaseOperation.DELETE_ALL;
    }
}
