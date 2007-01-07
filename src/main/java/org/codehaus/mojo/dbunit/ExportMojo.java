package org.codehaus.mojo.dbunit;


import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.ant.Export;
import org.dbunit.ant.Query;
import org.dbunit.ant.QuerySet;
import org.dbunit.ant.Table;
import org.dbunit.database.IDatabaseConnection;

/**
 * Execute DBUnit Export operation
 * 
 * @goal export
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @author <a href="mailto:topping@codehaus.org">Brian Topping</a>
 * @version $Id$
 * 
 */
public class ExportMojo
    extends AbstractDbUnitMojo
{
    /**
     * @parameter expression="${dest}" default-value="${project.build.directory}/dbunit/export.xml"
     */
    protected File dest;
    
    /**
     * @parameter expression="${format}" default-value="xml"
     */
    protected String format;
    
    /**
     * @parameter expression="${doctype}"
     */
    protected String doctype;
    
    /**
     * @parameter
     */
    protected Table [] tables;
    
    /**
     * @parameter
     */
    protected QuerySet [] querySets;
    
    /**
     * @parameter
     */
    protected Query [] queries;
    

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
                Export export = new Export();
                for ( int i = 0 ; queries != null && i < queries.length; ++ i ) 
                {
                    export.addQuery( (Query ) queries[i] );
                }
                for ( int i = 0 ; querySets != null && i < querySets.length; ++ i ) 
                {
                    export.addQuerySet( (QuerySet ) querySets[i] );
                }
                for ( int i = 0 ; tables != null && i < tables.length; ++ i ) 
                {
                    export.addTable( (Table ) tables[i] );
                }
                
                export.setDest( dest );
                export.setDoctype( doctype );
                export.setFormat( format );
                
                export.execute( connection );
            }
            finally
            {
                connection.close();
            }
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error executing export", e );
        }

    }
}
