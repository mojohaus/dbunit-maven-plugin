/**
 * 
 */
package org.codehaus.mojo.dbunit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id:$
 */
public abstract class AbstractDbUnitMojoTest
    extends TestCase
{
    protected Properties p;

    protected Connection c;

    protected void setUp()
        throws Exception
    {
        super.setUp();

        loadTestProperties();
        loadDriver();
        initDB();
 
    }

    protected void tearDown()
        throws Exception
    {
        if ( c != null )
        {
            c.close();
        }
        super.tearDown();
    }

    private void loadTestProperties()
        throws Exception
    {
        p = new Properties();
        p.load( getClass().getResourceAsStream( "/test.properties" ) );
    }
    
    private void loadDriver()
    {
        try
        {
            Class.forName( p.getProperty( "driver" ) );
        }
        catch ( Exception e )
        {
            System.out.println( "ERROR: failed to load driver." );
            e.printStackTrace();
        }
    }

    private Connection getConnection()
        throws SQLException
    {
        return DriverManager.getConnection( p.getProperty( "url" ), p.getProperty( "username" ), p
            .getProperty( "password" ) );
    }
    
    private void initDB()
        throws SQLException
    {
        c = getConnection();

        Statement st = c.createStatement();
        st.executeUpdate( "drop table person if exists" );
        st.executeUpdate( "create table person ( id integer, first_name varchar, last_name varchar)" );        
    }
    
    protected void populateMojoCommonConfiguration( AbstractDbUnitMojo mojo )
    {
        // populate parameters
        mojo.driver = p.getProperty( "driver" ) ;
        mojo.username = p.getProperty( "username" ) ;
        mojo.password =  p.getProperty( "password" );
        mojo.url =  p.getProperty( "url" ) ;
        mojo.schema =  p.getProperty( "schema" ) ;
        mojo.dataTypeFactoryName = p.getProperty( "dataTypeFactory", "org.dbunit.dataset.datatype.DefaultDataTypeFactory"  );
        mojo.supportBatchStatement = getBooleanProperty( "supportBatchStatement" ) ;
        mojo.useQualifiedTableNames = getBooleanProperty( "useQualifiedTableNames" );
        mojo.escapePattern = p.getProperty( "datatypeWarning" );

    }

    private boolean getBooleanProperty( String key )
    {
        return Boolean.valueOf( p.getProperty( key, "false" ) ).booleanValue();
    }    
    
    protected static File getBasedir()
    {
        return new File( System.getProperty( "basedir", System.getProperty( "user.dir" ) ));
    }
}
