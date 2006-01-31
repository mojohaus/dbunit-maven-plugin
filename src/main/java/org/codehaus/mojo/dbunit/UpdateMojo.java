package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal update
 */
public class UpdateMojo extends AbstractDbUnitMojo {
    DatabaseOperation getOperation() {
        return DatabaseOperation.UPDATE;
    }
}
