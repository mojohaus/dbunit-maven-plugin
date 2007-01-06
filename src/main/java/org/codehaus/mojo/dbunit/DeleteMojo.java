package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit DELETE Operation
 * 
 * @goal delete
 */
public class DeleteMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.DELETE;
    }

}
