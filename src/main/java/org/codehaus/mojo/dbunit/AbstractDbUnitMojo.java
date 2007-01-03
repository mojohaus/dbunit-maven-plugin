package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.AbstractMojo;
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
public abstract class AbstractDbUnitMojo extends AbstractMojo {
    /**
     * @parameter
     */
    private String driver;
    /**
     * @parameter
     */
    private String username;
    /**
     * @parameter
     */
    private String password;
    /**
     * @parameter
     */
    private String url;
    /**
     * @parameter
     */
    private String sourceData;
    /**
     * @parameter
     */
    private String sourceDataFormat;
    /**
     * @parameter
     */
    private String schema;
    /**
     * @parameter
     */
    private String dataTypeFactoryName = "org.dbunit.dataset.datatype.DefaultDataTypeFactory";
    /**
     * @parameter
     */
    private boolean supportBatchStatement;
    /**
     * @parameter
     */
    private boolean useQualifiedTableNames;
    /**
     * @parameter
     */
    private boolean datatypeWarning;
    /**
     * @parameter
     */
    private String escapePattern;

    public static final String FORMAT_FLAT = "flat";
    public static final String FORMAT_XML = "xml";
    public static final String FORMAT_DTD = "dtd";
    public static final String FORMAT_CSV = "csv";
    abstract DatabaseOperation getOperation();

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            IDatabaseConnection connection = createConnection();
            IDataSet dataset = getSrcDataSet(new File(sourceData), sourceDataFormat, false);

            try {
                getOperation().execute(connection, dataset);
            } finally {
                connection.close();
            }
            
        } catch (Exception e) {
            throw new MojoExecutionException("Caught Exception", e);
        }
    }

    protected IDataSet getSrcDataSet(File src, String format,
            boolean forwardonly) throws DatabaseUnitException
    {
        try
        {
            IDataSetProducer producer;
            if (format.equalsIgnoreCase(FORMAT_XML))
            {
                producer = new XmlProducer(new InputSource(src.toURL().toString()));
            }
            else if (format.equalsIgnoreCase(FORMAT_CSV))
            {
                producer = new CsvProducer(src);
            }
            else if (format.equalsIgnoreCase(FORMAT_FLAT))
            {
                producer = new FlatXmlProducer(new InputSource(src.toURL().toString()));
            }
            else if (format.equalsIgnoreCase(FORMAT_DTD))
            {
                producer = new FlatDtdProducer(new InputSource(src.toURL().toString()));
            }
            else
            {
                throw new IllegalArgumentException("Type must be either 'flat'(default), 'xml', 'csv' or 'dtd' but was: " + format);
            }

            if (forwardonly)
            {
                return new StreamingDataSet(producer);
            }
            return new CachedDataSet(producer);
        }
        catch (IOException e)
        {
            throw new DatabaseUnitException(e);
        }
    }


    IDatabaseConnection createConnection() throws Exception {

        if (driver == null) {
            throw new RuntimeException("Driver attribute must be set!");
        }
        if (username == null) {
            throw new RuntimeException("User Id attribute must be set!");
        }
        if (password == null) {
            throw new RuntimeException("Password attribute must be set!");
        }
        if (url == null) {
            throw new RuntimeException("Url attribute must be set!");
        }

        // Instanciate JDBC driver
        Class dc = Class.forName(driver);
        Driver driverInstance = (Driver) dc.newInstance();
        Properties info = new Properties();
        info.put("user", username);
        info.put("password", password);

        Connection conn = driverInstance.connect(url, info);

        if (conn == null) {
            // Driver doesn't understand the URL
            throw new SQLException("No suitable Driver for " + url);
        }
        conn.setAutoCommit(true);

        IDatabaseConnection connection = new DatabaseConnection(conn, schema);
        DatabaseConfig config = connection.getConfig();
        config.setFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, supportBatchStatement);
        config.setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, useQualifiedTableNames);
        config.setFeature(DatabaseConfig.FEATURE_DATATYPE_WARNING, datatypeWarning);
        config.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, escapePattern);
        config.setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY,
                new ForwardOnlyResultSetTableFactory());

        // Setup data type factory
        IDataTypeFactory dataTypeFactory = (IDataTypeFactory) Class.forName(dataTypeFactoryName).newInstance();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);

        return connection;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public String getSourceDataFormat() {
        return sourceDataFormat;
    }

    public void setSourceDataFormat(String sourceDataFormat) {
        this.sourceDataFormat = sourceDataFormat;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDataTypeFactoryName() {
        return dataTypeFactoryName;
    }

    public void setDataTypeFactoryName(String dataTypeFactoryName) {
        this.dataTypeFactoryName = dataTypeFactoryName;
    }

    public boolean isSupportBatchStatement() {
        return supportBatchStatement;
    }

    public void setSupportBatchStatement(boolean supportBatchStatement) {
        this.supportBatchStatement = supportBatchStatement;
    }

    public boolean isQualifiedTableNames() {
        return useQualifiedTableNames;
    }

    public void setQualifiedTableNames(boolean useQualifiedTableNames) {
        this.useQualifiedTableNames = useQualifiedTableNames;
    }

    public boolean isDatatypeWarning() {
        return datatypeWarning;
    }

    public void setDatatypeWarning(boolean datatypeWarning) {
        this.datatypeWarning = datatypeWarning;
    }

    public String getEscapePattern() {
        return escapePattern;
    }

    public void setEscapePattern(String escapePattern) {
        this.escapePattern = escapePattern;
    }
}
