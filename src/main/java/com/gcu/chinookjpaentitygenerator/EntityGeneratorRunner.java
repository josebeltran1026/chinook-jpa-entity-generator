package com.gcu.chinookjpaentitygenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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

        Path outputDirectory = Path.of(
                "src",
                "main",
                "java",
                "com",
                "gcu",
                "chinookjpaentitygenerator",
                "generated_entities");

        Files.createDirectories(outputDirectory);

        try (Connection connection = dataSource.getConnection()) {

            DatabaseMetaData metadata = connection.getMetaData();

            try (ResultSet tables = metadata.getTables(
                    null,
                    "public",
                    "%",
                    new String[] { "TABLE" })) {

                while (tables.next()) {

                    String tableName =
                            tables.getString("TABLE_NAME");

                    generateEntity(
                            metadata,
                            tableName,
                            outputDirectory);
                }
            }
        }
    }

    private void generateEntity(
            DatabaseMetaData metadata,
            String tableName,
            Path outputDirectory)
            throws SQLException, IOException {

        String className =
                toPascalCase(tableName);

        Set<String> primaryKeys =
                getPrimaryKeys(
                        metadata,
                        tableName);

        Map<String, ForeignKeyInfo> foreignKeys =
                getForeignKeys(
                        metadata,
                        tableName);

        Map<String, String> primaryKeyTypes =
                getPrimaryKeyTypes(
                        metadata,
                        tableName,
                        primaryKeys);

        boolean compositePrimaryKey =
                primaryKeys.size() > 1;

        StringBuilder code =
                new StringBuilder();

        code.append("""
                package com.gcu.chinookjpaentitygenerator.generated_entities;

                import jakarta.persistence.Column;
                import jakarta.persistence.Entity;
                import jakarta.persistence.GeneratedValue;
                import jakarta.persistence.GenerationType;
                import jakarta.persistence.Id;
                import jakarta.persistence.IdClass;
                import jakarta.persistence.JoinColumn;
                import jakarta.persistence.ManyToOne;
                import jakarta.persistence.Table;
                import jakarta.validation.constraints.NotNull;
                import jakarta.validation.constraints.Size;

                import java.math.BigDecimal;
                import java.time.LocalDateTime;

                """);

        code.append("@Entity\n");

        code.append("@Table(name = \"")
                .append(tableName)
                .append("\")\n");

        if (compositePrimaryKey) {

            code.append("@IdClass(")
                    .append(className)
                    .append("Id.class)\n");
        }

        code.append("public class ")
                .append(className)
                .append(" {\n\n");

        try (ResultSet columns =
                metadata.getColumns(
                        null,
                        "public",
                        tableName,
                        "%")) {

            while (columns.next()) {

                String columnName =
                        columns.getString(
                                "COLUMN_NAME");

                String typeName =
                        columns.getString(
                                "TYPE_NAME");

                int size =
                        columns.getInt(
                                "COLUMN_SIZE");

                boolean nullable =
                        columns.getInt(
                                "NULLABLE")
                                == DatabaseMetaData.columnNullable;

                String autoIncrement =
                        columns.getString(
                                "IS_AUTOINCREMENT");

                boolean generated =
                        "YES".equalsIgnoreCase(
                                autoIncrement);

                boolean primaryKey =
                        primaryKeys.contains(
                                columnName);

                String fieldName =
                        toCamelCase(
                                columnName);

                String javaType =
                        mapSqlTypeToJava(
                                typeName);

                if (primaryKey) {
                    code.append("    @Id\n");
                }

                if (generated) {

                    code.append(
                            "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                }

                if (!nullable) {
                    code.append(
                            "    @NotNull\n");
                }

                if (isTextType(typeName)
                        && size > 0) {

                    code.append(
                            "    @Size(max = ")
                            .append(size)
                            .append(")\n");
                }

                code.append(
                        "    @Column(name = \"")
                        .append(columnName)
                        .append("\")\n");

                code.append(
                        "    private ")
                        .append(javaType)
                        .append(" ")
                        .append(fieldName)
                        .append(";\n\n");

                /*
                 * If this column is a foreign key,
                 * also create a JPA relationship.
                 *
                 * The original scalar ID field is
                 * kept so the generated class still
                 * matches the actual table columns.
                 */
                ForeignKeyInfo foreignKey =
                        foreignKeys.get(
                                columnName);

                if (foreignKey != null) {

                    String referencedClass =
                            toPascalCase(
                                    foreignKey
                                            .getReferencedTable());

                    String relationshipField =
                            toCamelCase(
                                    foreignKey
                                            .getReferencedTable());

                    code.append(
                            "    @ManyToOne\n");

                    code.append(
                            "    @JoinColumn(name = \"")
                            .append(columnName)
                            .append(
                                    "\", referencedColumnName = \"")
                            .append(
                                    foreignKey
                                            .getReferencedColumn())
                            .append(
                                    "\", insertable = false, updatable = false)\n");

                    code.append(
                            "    private ")
                            .append(referencedClass)
                            .append(" ")
                            .append(relationshipField)
                            .append(";\n\n");
                }
            }
        }

        code.append("}\n");

        Path entityFile =
                outputDirectory.resolve(
                        className + ".java");

        Files.writeString(
                entityFile,
                code.toString());

        System.out.println(
                "Generated entity: "
                        + entityFile);

        /*
         * If the table has more than one
         * primary-key column, generate the
         * IdClass required by JPA.
         */
        if (compositePrimaryKey) {

            generateCompositeKeyClass(
                    className,
                    primaryKeyTypes,
                    outputDirectory);
        }
    }

    private void generateCompositeKeyClass(
            String className,
            Map<String, String> primaryKeyTypes,
            Path outputDirectory)
            throws IOException {

        String idClassName =
                className + "Id";

        StringBuilder code =
                new StringBuilder();

        code.append(
                "package com.gcu.chinookjpaentitygenerator.generated_entities;\n\n");

        code.append(
                "import java.io.Serializable;\n");

        code.append(
                "import java.util.Objects;\n\n");

        code.append(
                "public class ")
                .append(idClassName)
                .append(
                        " implements Serializable {\n\n");

        for (Map.Entry<String, String> entry
                : primaryKeyTypes.entrySet()) {

            String fieldName =
                    toCamelCase(
                            entry.getKey());

            code.append(
                    "    private ")
                    .append(entry.getValue())
                    .append(" ")
                    .append(fieldName)
                    .append(";\n");
        }

        code.append("\n");

        code.append(
                "    public ")
                .append(idClassName)
                .append("() {\n");

        code.append("    }\n\n");

        code.append(
                "    @Override\n");

        code.append(
                "    public boolean equals(Object o) {\n");

        code.append(
                "        if (this == o) {\n");

        code.append(
                "            return true;\n");

        code.append(
                "        }\n\n");

        code.append(
                "        if (!(o instanceof ")
                .append(idClassName)
                .append(" that)) {\n");

        code.append(
                "            return false;\n");

        code.append(
                "        }\n\n");

        code.append(
                "        return ");

        int index = 0;

        for (String columnName
                : primaryKeyTypes.keySet()) {

            String fieldName =
                    toCamelCase(
                            columnName);

            if (index > 0) {
                code.append(
                        "\n                && ");
            }

            code.append(
                    "Objects.equals(")
                    .append(fieldName)
                    .append(", that.")
                    .append(fieldName)
                    .append(")");

            index++;
        }

        code.append(";\n");
        code.append("    }\n\n");

        code.append(
                "    @Override\n");

        code.append(
                "    public int hashCode() {\n");

        code.append(
                "        return Objects.hash(");

        index = 0;

        for (String columnName
                : primaryKeyTypes.keySet()) {

            if (index > 0) {
                code.append(", ");
            }

            code.append(
                    toCamelCase(
                            columnName));

            index++;
        }

        code.append(");\n");
        code.append("    }\n");

        code.append("}\n");

        Path idFile =
                outputDirectory.resolve(
                        idClassName + ".java");

        Files.writeString(
                idFile,
                code.toString());

        System.out.println(
                "Generated composite key: "
                        + idFile);
    }

    private Set<String> getPrimaryKeys(
            DatabaseMetaData metadata,
            String tableName)
            throws SQLException {

        Set<String> primaryKeys =
                new HashSet<>();

        try (ResultSet resultSet =
                metadata.getPrimaryKeys(
                        null,
                        "public",
                        tableName)) {

            while (resultSet.next()) {

                primaryKeys.add(
                        resultSet.getString(
                                "COLUMN_NAME"));
            }
        }

        return primaryKeys;
    }

    private Map<String, String> getPrimaryKeyTypes(
            DatabaseMetaData metadata,
            String tableName,
            Set<String> primaryKeys)
            throws SQLException {

        Map<String, String> primaryKeyTypes =
                new LinkedHashMap<>();

        try (ResultSet columns =
                metadata.getColumns(
                        null,
                        "public",
                        tableName,
                        "%")) {

            while (columns.next()) {

                String columnName =
                        columns.getString(
                                "COLUMN_NAME");

                if (primaryKeys.contains(
                        columnName)) {

                    String typeName =
                            columns.getString(
                                    "TYPE_NAME");

                    primaryKeyTypes.put(
                            columnName,
                            mapSqlTypeToJava(
                                    typeName));
                }
            }
        }

        return primaryKeyTypes;
    }

    private Map<String, ForeignKeyInfo> getForeignKeys(
            DatabaseMetaData metadata,
            String tableName)
            throws SQLException {

        Map<String, ForeignKeyInfo> foreignKeys =
                new HashMap<>();

        try (ResultSet resultSet =
                metadata.getImportedKeys(
                        null,
                        "public",
                        tableName)) {

            while (resultSet.next()) {

                String fkColumn =
                        resultSet.getString(
                                "FKCOLUMN_NAME");

                String referencedTable =
                        resultSet.getString(
                                "PKTABLE_NAME");

                String referencedColumn =
                        resultSet.getString(
                                "PKCOLUMN_NAME");

                foreignKeys.put(
                        fkColumn,
                        new ForeignKeyInfo(
                                referencedTable,
                                referencedColumn));
            }
        }

        return foreignKeys;
    }

    private String mapSqlTypeToJava(
            String sqlType) {

        return switch (
                sqlType.toLowerCase()) {

            case "int2",
                 "smallint" ->
                    "Short";

            case "int4",
                 "integer",
                 "serial" ->
                    "Integer";

            case "int8",
                 "bigint",
                 "bigserial" ->
                    "Long";

            case "numeric",
                 "decimal" ->
                    "BigDecimal";

            case "timestamp",
                 "timestamp without time zone" ->
                    "LocalDateTime";

            case "varchar",
                 "text",
                 "char",
                 "bpchar" ->
                    "String";

            case "bool",
                 "boolean" ->
                    "Boolean";

            case "float4",
                 "real" ->
                    "Float";

            case "float8",
                 "double precision" ->
                    "Double";

            default ->
                    "String";
        };
    }

    private boolean isTextType(
            String sqlType) {

        return switch (
                sqlType.toLowerCase()) {

            case "varchar",
                 "char",
                 "bpchar" -> true;

            default -> false;
        };
    }

    private String toPascalCase(
            String value) {

        StringBuilder result =
                new StringBuilder();

        boolean uppercaseNext =
                true;

        for (char character
                : value.toCharArray()) {

            if (character == '_') {

                uppercaseNext =
                        true;

                continue;
            }

            if (uppercaseNext) {

                result.append(
                        Character.toUpperCase(
                                character));

                uppercaseNext =
                        false;

            } else {

                result.append(
                        character);
            }
        }

        return result.toString();
    }

    private String toCamelCase(
            String value) {

        String pascalCase =
                toPascalCase(
                        value);

        return Character.toLowerCase(
                pascalCase.charAt(0))
                + pascalCase.substring(1);
    }

    private static class ForeignKeyInfo {

        private final String referencedTable;
        private final String referencedColumn;

        ForeignKeyInfo(
                String referencedTable,
                String referencedColumn) {

            this.referencedTable =
                    referencedTable;

            this.referencedColumn =
                    referencedColumn;
        }

        String getReferencedTable() {
            return referencedTable;
        }

        String getReferencedColumn() {
            return referencedColumn;
        }
    }
}