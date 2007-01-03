package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal delete
 */
public class DeleteMojo
    extends AbstractDbUnitMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.DELETE;
    }

}
