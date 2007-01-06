package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit CLEAN_INSERT Operation
 * @goal clean-insert
 * @description Clean tables and insert values from file
 */
public class CleanInsertMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.CLEAN_INSERT;
    }
}
