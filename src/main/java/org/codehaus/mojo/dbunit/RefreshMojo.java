package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal refresh
 */
public class RefreshMojo
    extends AbstractDbUnitMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.REFRESH;
    }
}
