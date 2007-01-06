package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit TRUNCATE_TABLE Operation
 * 
 * @goal truncate-table
 */
public class TruncateTableMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.TRUNCATE_TABLE;
    }
}
