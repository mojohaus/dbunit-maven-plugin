package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvProducer;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdProducer;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlProducer;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

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

    /**
     * The schema name that tables can be found under.
     * 
     * @parameter
     */
    private String schema;

    /**
     * Set the DataType factory to add support for non-standard database vendor data types.
     * 
     * @parameter default-value="org.dbunit.dataset.datatype.DefaultDataTypeFactory"
     */
    private String dataTypeFactoryName = "org.dbunit.dataset.datatype.DefaultDataTypeFactory";

    /**
     * @parameter
     */
    private boolean supportBatchStatement;

    /**
     * Enable or disable multiple schemas support by prefixing table names with the schema name.
     * 
     * @parameter
     */
    private boolean useQualifiedTableNames;

    /**
     * @parameter
     */
    private boolean datatypeWarning;

    /**
     * escapePattern
     * 
     * @parameter
     */
    private String escapePattern;

    public static final String FORMAT_FLAT = "flat";

    public static final String FORMAT_XML = "xml";

    public static final String FORMAT_DTD = "dtd";

    public static final String FORMAT_CSV = "csv";

    abstract DatabaseOperation getOperation();

    IDatabaseConnection createConnection()
        throws Exception
    {

        // Instantiate JDBC driver
        Class dc = Class.forName( driver );
        Driver driverInstance = (Driver) dc.newInstance();
        Properties info = new Properties();
        info.put( "user", username );

        if ( password != null )
        {
            info.put( "password", password );
        }

        Connection conn = driverInstance.connect( url, info );

        if ( conn == null )
        {
            // Driver doesn't understand the URL
            throw new SQLException( "No suitable Driver for " + url );
        }
        conn.setAutoCommit( true );

        IDatabaseConnection connection = new DatabaseConnection( conn, schema );
        DatabaseConfig config = connection.getConfig();
        config.setFeature( DatabaseConfig.FEATURE_BATCHED_STATEMENTS, supportBatchStatement );
        config.setFeature( DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, useQualifiedTableNames );
        config.setFeature( DatabaseConfig.FEATURE_DATATYPE_WARNING, datatypeWarning );
        config.setProperty( DatabaseConfig.PROPERTY_ESCAPE_PATTERN, escapePattern );
        config.setProperty( DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY, new ForwardOnlyResultSetTableFactory() );

        // Setup data type factory
        IDataTypeFactory dataTypeFactory = (IDataTypeFactory) Class.forName( dataTypeFactoryName ).newInstance();
        config.setProperty( DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory );

        return connection;
    }

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
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

    public String getSchema()
    {
        return schema;
    }

    public void setSchema( String schema )
    {
        this.schema = schema;
    }

    public String getDataTypeFactoryName()
    {
        return dataTypeFactoryName;
    }

    public void setDataTypeFactoryName( String dataTypeFactoryName )
    {
        this.dataTypeFactoryName = dataTypeFactoryName;
    }

    public boolean isSupportBatchStatement()
    {
        return supportBatchStatement;
    }

    public void setSupportBatchStatement( boolean supportBatchStatement )
    {
        this.supportBatchStatement = supportBatchStatement;
    }

    public boolean isQualifiedTableNames()
    {
        return useQualifiedTableNames;
    }

    public void setQualifiedTableNames( boolean useQualifiedTableNames )
    {
        this.useQualifiedTableNames = useQualifiedTableNames;
    }

    public boolean isDatatypeWarning()
    {
        return datatypeWarning;
    }

    public void setDatatypeWarning( boolean datatypeWarning )
    {
        this.datatypeWarning = datatypeWarning;
    }

    public String getEscapePattern()
    {
        return escapePattern;
    }

    public void setEscapePattern( String escapePattern )
    {
        this.escapePattern = escapePattern;
    }
}
