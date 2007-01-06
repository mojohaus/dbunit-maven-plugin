package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;
 
/** 
 * Execute DBUnit TRUNCATE_TABLE Operation
 * 
 * @goal truncate-table
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @author <a href="mailto:topping@codehaus.org">Brian Topping</a>
 * @version $Id:$
 */
public class TruncateTableMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.TRUNCATE_TABLE;
    }
}
