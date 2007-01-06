package org.codehaus.mojo.dbunit;

import java.io.FileOutputStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * Export a table and its dependents to a file
 * @goal export-table
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id$
 */

public abstract class ExportTableMojo
    extends AbstractDatabaseExportMojo
{
    /**
     * Table name 
     * @parameter expression="${table}
     * @required
     */

    private String table;

    protected IDataSet createDataSet( IDatabaseConnection connection )
        throws Exception
    {
        String[] depTableNames = TablesDependencyHelper.getAllDependentTables( connection, table );
        return connection.createDataSet( depTableNames );
    }

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        super.execute();

        try
        {
            IDatabaseConnection connection = createConnection();

            try
            {
                String[] depTableNames = TablesDependencyHelper.getAllDependentTables( connection, table );
                IDataSet depDataSet = connection.createDataSet( depTableNames );
                FlatXmlDataSet.write( depDataSet, new FileOutputStream( this.outputFile ) );
            }
            finally
            {
                connection.close();
            }

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error exporting tables", e );
        }
    }
}
