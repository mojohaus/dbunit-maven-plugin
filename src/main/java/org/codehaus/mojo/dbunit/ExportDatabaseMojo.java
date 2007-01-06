package org.codehaus.mojo.dbunit;

import java.io.FileOutputStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * Export all tables into a file
 * @goal export-database
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id$
 */
public class ExportDatabaseMojo
    extends AbstractDatabaseExportMojo
{
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        super.execute();
        
        try
        {
            IDatabaseConnection connection = createConnection();

            try
            {
                IDataSet fullDataSet = connection.createDataSet();
                FlatXmlDataSet.write( fullDataSet, new FileOutputStream( this.outputFile ) );
            }
            finally
            {
                connection.close();
            }

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error exporting full database", e );
        }
    }
}
