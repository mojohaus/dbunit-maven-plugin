package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal export
 */
public class ExportMojo
    extends AbstractDbUnitMojo
{
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        throw new  MojoExecutionException( "To be implemented " );
    }
}
