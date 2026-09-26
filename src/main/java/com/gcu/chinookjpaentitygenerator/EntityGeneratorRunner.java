package com.gcu.chinookjpaentitygenerator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EntityGeneratorRunner implements CommandLineRunner {

    private final DataSource dataSource;

    public EntityGeneratorRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {

            DatabaseMetaData metadata = connection.getMetaData();

            try (ResultSet tables = metadata.getTables(
                    null,
                    "public",
                    "%",
                    new String[] { "TABLE" })) {

                while (tables.next()) {

                    String tableName = tables.getString("TABLE_NAME");

                    Set<String> primaryKeys = new HashSet<>();

                    try (ResultSet pkResults = metadata.getPrimaryKeys(
                            null,
                            "public",
                            tableName)) {

                        while (pkResults.next()) {
                            primaryKeys.add(
                                    pkResults.getString("COLUMN_NAME"));
                        }
                    }

                    System.out.println();
                    System.out.println("TABLE: " + tableName);

                    try (ResultSet columns = metadata.getColumns(
                            null,
                            "public",
                            tableName,
                            "%")) {

                        while (columns.next()) {

                            String columnName =
                                    columns.getString("COLUMN_NAME");

                            String dataType =
                                    columns.getString("TYPE_NAME");

                            int maxLength =
                                    columns.getInt("COLUMN_SIZE");

                            boolean nullable =
                                    columns.getInt("NULLABLE")
                                            == DatabaseMetaData.columnNullable;

                            boolean primaryKey =
                                    primaryKeys.contains(columnName);

                            System.out.println(
                                    "  COLUMN: " + columnName
                                    + " | TYPE: " + dataType
                                    + " | LENGTH: " + maxLength
                                    + " | NULLABLE: " + nullable
                                    + " | PRIMARY KEY: " + primaryKey
                            );
                        }
                    }

                    try (ResultSet foreignKeys = metadata.getImportedKeys(
                            null,
                            "public",
                            tableName)) {

                        while (foreignKeys.next()) {

                            String fkColumn =
                                    foreignKeys.getString("FKCOLUMN_NAME");

                            String referencedTable =
                                    foreignKeys.getString("PKTABLE_NAME");

                            String referencedColumn =
                                    foreignKeys.getString("PKCOLUMN_NAME");

                            System.out.println(
                                    "  FOREIGN KEY: " + fkColumn
                                    + " -> "
                                    + referencedTable
                                    + "."
                                    + referencedColumn
                            );
                        }
                    }
                }
            }
        }
    }
}