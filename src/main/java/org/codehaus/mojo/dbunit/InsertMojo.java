package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit INSERT Operation
 * 
 * @goal insert
 */
public class InsertMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.INSERT;
    }
}
