package org.codehaus.mojo.dbunit;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author <a href="mailto:dantran@gmail.com">Dan Tran</a>
 * @version $Id:$
 */
public class OperationMojoTest
    extends AbstractDbUnitMojoTest
{

    private OperationMojo mojo;
    private File outputFile;

    
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        mojo = new OperationMojo();
        this.populateMojoCommonConfiguration( mojo );
        
        outputFile = new File( getBasedir(), "target/export.xml" );
    }
    
    
    public void testCleanInsertOperation()
        throws Exception
    {
        mojo.src = new File( p.getProperty( "xmlDataSource" ) );
        mojo.format = "xml";
        mojo.type = "CLEAN_INSERT";
        mojo.execute();
        
        ExportDatabaseMojo export = new ExportDatabaseMojo();
        this.populateMojoCommonConfiguration( export );
        export.outputFile = outputFile;
        export.execute();
        
        mojo.src = outputFile;
        mojo.execute();
        
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery( "select count(*) from person" );
        rs.next();
        assertEquals( 2, rs.getInt(1) );        
    }

}
