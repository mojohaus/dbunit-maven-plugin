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
    
    public void testCleanInsertOperation()
        throws Exception
    {
        //init database with fixed data
        OperationMojo operation = new OperationMojo();
        this.populateMojoCommonConfiguration( operation );
        operation.src = new File( p.getProperty( "xmlDataSource" ) );
        operation.format = "xml";
        operation.type = "CLEAN_INSERT";
        operation.execute();
        
        //export database to a file
        File exportFile = new File( getBasedir(), "target/export.xml" );
        ExportMojo export = new ExportMojo();
        this.populateMojoCommonConfiguration( export );
        export.dest = exportFile;
        export.format = "xml";
        export.execute();
        
        //then import it back to DB
        operation.src = exportFile;
        operation.execute();
        
        //check to makesure we have 2 rows
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery( "select count(*) from person" );
        rs.next();
        assertEquals( 2, rs.getInt(1) );        
    }

}
