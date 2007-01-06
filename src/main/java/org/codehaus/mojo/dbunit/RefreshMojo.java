package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit REFRESH Operation

 * @goal refresh
 */
public class RefreshMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.REFRESH;
    }
}
