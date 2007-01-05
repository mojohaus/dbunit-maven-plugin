package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.AbstractMojo;

/**
 */
public abstract class AbstractDbUnitMojo
    extends AbstractMojo
{
    /**
     * The class name of the JDBC driver to be used.
     * @parameter
     * @required
     */
    protected String driver;

    /**
     * The user name used to connect to the database.
     * @parameter
     * @required
     */
    protected String username;

    /**
     * The password of the user connecting to the database.
     * @parameter
     */
    protected String password;

    /**
     * The JDBC URL for the database to access, e.g. jdbc:db2:SAMPLE. 
     * @parameter
     * @required
     */
    protected String url;


    public static final String FORMAT_FLAT = "flat";

    public static final String FORMAT_XML = "xml";

    public static final String FORMAT_DTD = "dtd";

    public static final String FORMAT_CSV = "csv";


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

}
