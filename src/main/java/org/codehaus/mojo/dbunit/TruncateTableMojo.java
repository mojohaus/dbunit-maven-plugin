package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * @goal truncate-table
 */
public class TruncateTableMojo
    extends AbstractDbUnitMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.TRUNCATE_TABLE;
    }
}
