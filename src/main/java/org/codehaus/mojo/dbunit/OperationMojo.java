package org.codehaus.mojo.dbunit;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.ant.Operation;
import org.dbunit.database.IDatabaseConnection;

/**
 * Execute DBUnit's Database Operation with an extern dataset file.
 * 
 * @goal operation
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @author <a href="mailto:topping@codehaus.org">Brian Topping</a>
 * @version $Id$
 * 
 */
public class OperationMojo
    extends AbstractDbUnitMojo
{
    /**
     * Type of Database operation to perform. Supported types are UPDATE, 
     * INSERT, DELETE, DELETE_ALL, REFRESH, CLEAN_INSERT, MSSQL_INSERT, 
     * MSSQL_REFRESH, MSSQL_CLEAN_INSERT
     * 
     * @parameter expression="type" 
     * @required
     */
    protected String type;

    /**
     * When true, place the entired operation in one transaction
     * @parameter expression="transaction" default-value="false"
     * 
     */
    protected boolean transaction;

    /**
     * DataSet file
     * 
     * @parameter expression="${src}"
     * @required
     */
    protected File src;

    /**
     * src format type. Valid type are: flat, xml, csv, and dtd
     * 
     * @parameter expression="${format}" default-value="xml";
     * @required
     */
    protected String format;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( skip )
        {
            return;
        }

        super.execute();
        
        try
        {
            IDatabaseConnection connection = createConnection();
            try
            {
                Operation op = new Operation();
                op.setFormat( format );
                op.setSrc( src );
                op.setTransaction( transaction );
                op.setType( type );
                op.execute( connection );
            }
            finally
            {
                connection.close();
            }
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error executing " + type, e );
        }

    }
}
