/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabricio
 */
public class DBConexao {

    private static String serverName;
    private static String portNumber;
    private static String database;
    private static String url;
    private static String username;
    private static String password;
    
    private static Connection globalConn = null;
    
    
    
    private static final String CONNECTION_CONFIG_FILE = "./Configuracoes.txt";
    
    
    public DBConexao() {

    }

    public static Connection openGlobalConnection(){
        
        if(isGlobalConnectionClosed()){
            
            globalConn = openConnection();
        }
        
        return globalConn;
    }
    
    public static boolean isGlobalConnectionClosed(){
        
        try {        
            return (globalConn == null || globalConn.isClosed());
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static Connection getGlobalConnection(){
        
        return globalConn;        
    }
    
    public static void closeGlobalConnection(){
        
        if (!isGlobalConnectionClosed()) {
            
            closeConnection(globalConn);
        }
    }
    
    
    public static void retrieveConnectionConfig() {
        
        try {
            ArrayList<String> configs = util.FileReader.readFile(CONNECTION_CONFIG_FILE);
            
            if (configs.size() == 5) {
                
                for (int i = 0; i < configs.size(); i++) {
                    
                    String config = configs.get(i).replace(" ", "");
                    config = config.substring(config.lastIndexOf("=") + 1);
                    configs.set(i, config);
                }
                
                serverName = configs.get(0);//"127.0.0.1";
                portNumber = configs.get(1);//"3306";
                database = configs.get(2);//"projeto_pesquisa";
                
                username = configs.get(3);//"root";
                password = configs.get(4);//"root"; 
                
                url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + database;

                //JOptionPane.showMessageDialog(null, "DBConexao.java");
            } else {
                throw new IOException("O arquivo de configurações é inválido.");
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Problema durante conexão com servidor. " + ex.getMessage(), 
                    "Problema encontrado no servidor.", JOptionPane.ERROR_MESSAGE);
            System.out.println("Problema durante conexão com banco de dados. " + ex.getMessage());
        }
        
    }
    
    
    public static Connection openConnection() {
        Connection connection;
        String driverName = "com.mysql.jdbc.Driver";
        
        try {
            
            Class.forName(driverName);

            connection = (Connection) DriverManager.getConnection(url, username, password);

            if (connection == null) {
                System.out.println("Falha na conexão com banco de dados.");
            }
            
            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado. " + e.getMessage());
            return null;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema durante conexão com servidor. " + ex.getMessage(), 
                    "Problema encontrado no servidor.", JOptionPane.ERROR_MESSAGE);
            System.out.println("Problema durante conexão com banco de dados. " + ex.getMessage());
            return null;
        } 
    }

    public static void closeConnection(java.sql.Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println("Falha ao encerrar conexão com banco de dados." + ex.getMessage());
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getPortNumber() {
        return portNumber;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }   
}
