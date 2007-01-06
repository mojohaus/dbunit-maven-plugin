package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvProducer;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdProducer;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlProducer;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 */
public abstract class AbstractDatabaseOperationMojo
    extends AbstractDbUnitMojo
{

    /**
     * import data file
     * 
     * @parameter
     * @required
     */
    private File sourceData;

    /**
     * sourceData format type. Valid type are: flat, xml, csv, and dtd
     * 
     * @parameter default-value="xml";
     * @required
     */
    private String sourceDataFormat;

    abstract DatabaseOperation getOperation();

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        super.execute();
        
        try
        {
            IDatabaseConnection connection = createConnection();
            IDataSet dataset = getSrcDataSet( sourceData, sourceDataFormat, false );

            try
            {
                getOperation().execute( connection, dataset );
            }
            finally
            {
                connection.close();
            }

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error executing " + getOperation().toString(), e );
        }
    }

    protected void closeConnection ( IDatabaseConnection c )
        throws MojoExecutionException
    {
        try 
        {
            c.close();
        }
        catch ( SQLException e )
        {
            throw new MojoExecutionException( "Unable to close connection", e );
        }
        
    }
    protected IDataSet getSrcDataSet( File src, String format, boolean forwardonly )
        throws DatabaseUnitException
    {
        try
        {
            IDataSetProducer producer;
            if ( format.equalsIgnoreCase( FORMAT_XML ) )
            {
                producer = new XmlProducer( new InputSource( src.toURL().toString() ) );
            }
            else if ( format.equalsIgnoreCase( FORMAT_CSV ) )
            {
                producer = new CsvProducer( src );
            }
            else if ( format.equalsIgnoreCase( FORMAT_FLAT ) )
            {
                producer = new FlatXmlProducer( new InputSource( src.toURL().toString() ) );
            }
            else if ( format.equalsIgnoreCase( FORMAT_DTD ) )
            {
                producer = new FlatDtdProducer( new InputSource( src.toURL().toString() ) );
            }
            else
            {
                throw new IllegalArgumentException(
                                                    "Type must be either 'flat'(default), 'xml', 'csv' or 'dtd' but was: "
                                                                    + format );
            }

            if ( forwardonly )
            {
                return new StreamingDataSet( producer );
            }
            return new CachedDataSet( producer );
        }
        catch ( IOException e )
        {
            throw new DatabaseUnitException( e );
        }
    }

    public File getSourceData()
    {
        return sourceData;
    }

    public void setSourceData( File sourceData )
    {
        this.sourceData = sourceData;
    }

    public String getSourceDataFormat()
    {
        return sourceDataFormat;
    }

    public void setSourceDataFormat( String sourceDataFormat )
    {
        this.sourceDataFormat = sourceDataFormat;
    }

}
