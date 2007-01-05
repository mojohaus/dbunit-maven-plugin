package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
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
