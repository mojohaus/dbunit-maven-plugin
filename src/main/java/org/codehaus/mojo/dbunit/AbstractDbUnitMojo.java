package org.codehaus.mojo.dbunit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.IDataTypeFactory;

/**
 * Common configurations for all DBUnit operations
 */
public abstract class AbstractDbUnitMojo
    extends AbstractMojo
{

    public static final String FORMAT_FLAT = "flat";

    public static final String FORMAT_XML = "xml";

    public static final String FORMAT_DTD = "dtd";

    public static final String FORMAT_CSV = "csv";

    /**
     * The class name of the JDBC driver to be used.
     * 
     * @parameter
     * @required
     */
    protected String driver;

    /**
     * The user name used to connect to the database.
     * 
     * @parameter
     * @required
     */
    protected String username;

    /**
     * The password of the user connecting to the database.
     * 
     * @parameter
     */
    protected String password;

    /**
     * The JDBC URL for the database to access, e.g. jdbc:db2:SAMPLE.
     * 
     * @parameter
     * @required
     */
    protected String url;

    /**
     * The schema name that tables can be found under.
     * 
     * @parameter
     */
    protected String schema;

    /**
     * Set the DataType factory to add support for non-standard database vendor data types.
     * 
     * @parameter default-value="org.dbunit.dataset.datatype.DefaultDataTypeFactory"
     */
    protected String dataTypeFactoryName = "org.dbunit.dataset.datatype.DefaultDataTypeFactory";

    /**
     * @parameter
     */
    protected boolean supportBatchStatement;

    /**
     * Enable or disable multiple schemas support by prefixing table names with the schema name.
     * 
     * @parameter
     */
    protected boolean useQualifiedTableNames;

    /**
     * @parameter
     */
    protected boolean datatypeWarning;

    /**
     * escapePattern
     * 
     * @parameter
     */
    protected String escapePattern;

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

    ///////////////////////////////////////////////////////////////////////////////
    //  UNIT TEST HELPERS 
    ///////////////////////////////////////////////////////////////////////////////
    
    
    public String getDriver()
    {
        return driver;
    }

    public void setDriver( String driver )
    {
        this.driver = driver;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
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
