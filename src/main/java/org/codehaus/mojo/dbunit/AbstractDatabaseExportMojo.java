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
     * @parameter expression="${outputDirectory} default-value="${project.build.directory}/dbunit/export.xml
     */

    protected File outputFile;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        outputFile.getParentFile().mkdirs();
    }

}
