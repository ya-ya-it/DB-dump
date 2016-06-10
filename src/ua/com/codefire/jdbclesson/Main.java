/*
 * CodeFireUA public license.
 */
package ua.com.codefire.jdbclesson;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CodeFireUA <edu@codefire.com.ua>
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Class.forName(com.mysql.jdbc.Driver.class.getName());
        List<String> tableList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "12345")) {

            ResultSet rsTables = conn.createStatement().executeQuery("SHOW TABLES");

            while (rsTables.next()) {

                String tableName = rsTables.getString(1);
                tableList.add(tableName);

                ResultSet rsInsert = conn.createStatement()
                        .executeQuery("SELECT * FROM " + tableName);

                ResultSetMetaData meta = rsInsert.getMetaData();
                int cols = meta.getColumnCount();

                while (rsInsert.next()) {
                    for (int i = 1; i <= cols; i++) {

                        String insert = rsInsert.getString(i);

                        tableList.add(insert);
                    }

                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        String listString = "";

        for (String s : tableList) {
            listString += s + "\t";
        }

        System.out.println(listString);

        try (FileWriter writer = new FileWriter("D:\\backup.dump")) {
            writer.write(listString);
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

}
