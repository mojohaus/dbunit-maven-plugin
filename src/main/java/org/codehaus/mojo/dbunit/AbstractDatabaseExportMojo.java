package org.codehaus.mojo.dbunit;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * Base class of all export mojos
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id$
 * 
 */
public abstract class AbstractDatabaseExportMojo
    extends AbstractDbUnitMojo
{
    /**
     * Location of exported output file
     * 
     * @parameter expression="${outputFile}" default-value="${project.build.directory}/dbunit/export.xml"
     * @required
     */

    protected File outputFile;
    
    protected abstract IDataSet createDataSet( IDatabaseConnection connection ) throws Exception;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( skip )
        {
            return;
        }
        
        super.execute();
        
        outputFile.getParentFile().mkdirs();
        
        try
        {
            IDatabaseConnection connection = createConnection();

            try
            {
                IDataSet dataSet = createDataSet( connection );
                FlatXmlDataSet.write( dataSet, new FileOutputStream( this.outputFile ) );
            }
            finally
            {
                connection.close();
            }
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error exporting database", e );
        }
    }

}
