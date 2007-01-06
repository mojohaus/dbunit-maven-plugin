package org.codehaus.mojo.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * Execute DBUnit REFRESH Operation

 * @goal refresh
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @author <a href="mailto:topping@codehaus.org">Brian Topping</a>
 * @version $Id:$
 * 
 */
public class RefreshMojo
    extends AbstractDatabaseOperationMojo
{
    DatabaseOperation getOperation()
    {
        return DatabaseOperation.REFRESH;
    }
}
