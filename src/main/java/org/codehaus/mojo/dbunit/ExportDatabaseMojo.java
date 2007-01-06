package org.codehaus.mojo.dbunit;


import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

/**
 * Export all tables into a file
 * @goal export-database
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id$
 */
public class ExportDatabaseMojo
    extends AbstractDatabaseExportMojo
{
    protected IDataSet createDataSet( IDatabaseConnection connection )
        throws Exception
    {
        return connection.createDataSet();
    }
}
