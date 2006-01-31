package org.codehaus.mojo.dbunit;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: topping
 * Date: Jan 26, 2006
 * Time: 10:15:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUnitMojoTest extends TestCase {
    private Properties p;

    protected void setUp() throws Exception {
        super.setUp();
        p = new Properties();
        p.load(getClass().getResourceAsStream("/test.properties"));

        try {
            Class.forName(p.getProperty("driver"));
        } catch (Exception e) {
            System.out.println("ERROR: failed to load driver.");
            e.printStackTrace();
            return;
        }

        Connection c = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
        Statement st = c.createStatement();
        st.executeUpdate("drop table person if exists");
        st.executeUpdate("create table person ( id integer, first_name varchar, last_name varchar)");
        c.close();
    }

    public void testMojo() throws Exception {

        CleanInsertMojo mojo = new CleanInsertMojo();

        // populate parameters
        mojo.setDriver(p.getProperty("driver"));
        mojo.setUsername(p.getProperty("username"));
        mojo.setPassword(p.getProperty("password"));
        mojo.setUrl(p.getProperty("url"));
        mojo.setSourceData(p.getProperty("xmlDataSource"));
        mojo.setSourceDataFormat(p.getProperty("xmlDataSourceFormat"));
        mojo.setSchema(p.getProperty("schema"));
        mojo.setDataTypeFactoryName(p.getProperty("dataTypeFactory", "org.dbunit.dataset.datatype.DefaultDataTypeFactory"));
        mojo.setSupportBatchStatement(getBooleanProperty("supportBatchStatement"));
        mojo.setQualifiedTableNames(getBooleanProperty("useQualifiedTableNames"));
        mojo.setEscapePattern(p.getProperty("datatypeWarning"));

        mojo.execute();

        Connection c = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("select count(*) from person");
        assert(rs.getInt(1) == 2);
        c.close();
    }

    private boolean getBooleanProperty(String key) {
        return Boolean.valueOf(p.getProperty(key, "false")).booleanValue();
    }

}
