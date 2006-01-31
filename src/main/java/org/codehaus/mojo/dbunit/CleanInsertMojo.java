package org.codehaus.mojo.dbunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;

/**
 * @goal clean-insert
 * @description Clean tables and insert values from file
 */
public class CleanInsertMojo extends AbstractDbUnitMojo {
    DatabaseOperation getOperation() {
        return DatabaseOperation.CLEAN_INSERT;
    }
}
