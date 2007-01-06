package org.codehaus.mojo.dbunit;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Base class of all export mojos
 */
public abstract class AbstractDatabaseExportMojo
    extends AbstractDbUnitMojo
{
    /**
     * Location of exported output file
     * 
     * @parameter expression="${outputFile} default-value="${project.build.directory}/dbunit/export.xml"
     * @required
     */

    protected File outputFile;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        super.execute();
        outputFile.getParentFile().mkdirs();
    }

}
