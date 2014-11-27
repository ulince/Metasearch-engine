package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulysses
 */
public class DBConnection {
    
    private String myUrl;
    private Connection conn;

    public DBConnection() {
        myUrl = "jdbc:mysql://localhost:3306/documentos";

    }
    
    public void SP(int nuevo,int anterior, String tabla) throws SQLException {
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            CallableStatement cs = null;
            
            if(nuevo == 0 && anterior == 0){
                cs = this.conn.prepareCall("{call restore_mrdd()}");
            }else{
                cs = this.conn.prepareCall("{call alterar_mrdd(" + nuevo + "," + anterior + ")}");
            }
            int i = cs.executeUpdate();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
        }
    }
    
    //Ejecuta la stored procedure medida y regresa el resultado como un objeto de tipo Query; el resultado esta en 
    //el field con el mismo nombre de la medida
    public Query StoredProc(String medida) throws SQLException {
        Query q = new Query();
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            CallableStatement cs = null;
            cs = this.conn.prepareCall("{call " + medida + "()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                q.setFrecuencia(rs.getFloat(medida));
                q.setQuery_id(rs.getInt("query_id1"));
            }
            return q;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return q;
        } finally {
            conn.close();
        }
    }
    
    //Este método lee la base de datos y regresa el resultado como un List de tipo Query
    //str es el comando SQL
    public List<Query> readDataBase(String str) throws SQLException {

        List<Query> list = new ArrayList<Query>();
        try {
            conn = DriverManager.getConnection(myUrl, "root",
                    "admin");
            // the mysql select statement
            String query = str;

            // Statements allow to issue SQL queries to the database
            Statement statement = conn.createStatement();
            // Result set get the result of the SQL query
            ResultSet rs = statement.executeQuery(query);
            // writeResultSet(resultSet);	

            //Tomar las stopwords del ResultSet y ponerlos en una lista
            while (rs.next()) {
                list.add(new Query(rs.getInt("query_id"),rs.getString("termino"),rs.getFloat("frecuencia")));
            }
            return list;
        } catch (Exception e) {
            //System.err.println("Got an exception!");
            //System.err.println(e.getMessage());		
            return list;
        } finally {
            conn.close();
        }
    }
    
     /*Este metodo lee un representativo de la base de datos y regresa un objeto de tipo
      Representativo*/
    public Representativo readDataBaseR(String str) throws SQLException {

        Representativo r = null;
        try {
            conn = DriverManager.getConnection(myUrl, "root",
                    "admin");
            // the mysql select statement
            String query = str;

            // Statements allow to issue SQL queries to the database
            Statement statement = conn.createStatement();
            // Result set get the result of the SQL query
            ResultSet rs = statement.executeQuery(query);
            // writeResultSet(resultSet);	

            //Tomar las stopwords del ResultSet y ponerlos en una lista
            while (rs.next()) {
                r = new Representativo(rs.getInt("query_id"));
                r.setGoogleProp(rs.getDouble("googleProp"));
                r.setYahooProp(rs.getDouble("yahooProp"));
                r.setBingProp(rs.getDouble("bingProp"));
                r.setMotorProp(rs.getDouble("motorProp"));
            }
            return r;
        } catch (Exception e) {
            //System.err.println("Got an exception!");
            System.err.println(e.getMessage());		
            return r;
        } finally {
            conn.close();
        }
    }
    
    //Insertar una tupla a la tabla consulta por cada palabra que tenga la consulta. Todas se crean con 1 como uri
    //Para ejecutar este metodo, fue necesario parsear la consulta y crear un objeto de tipo Query por cada palabra,
    //y ejecutar este metodo por cada palabra
    public void InsertQuery1(Query q) throws SQLException {
        String query = "update consulta_mrdd set frecuencia = frecuencia+1 where termino = ?";
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, q.getTermino());
            //preparedStmt.setString(2, q.getRaiz());
            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception e) {
            //Do something
        } finally {
            conn.close();
        }
    }
    
    //Insertar una tupla a la tabla consulta por cada palabra que tenga la consulta. Todas se crean con 1 como uri
    //Para ejecutar este metodo, fue necesario parsear la consulta y crear un objeto de tipo Query por cada palabra,
    //y ejecutar este metodo por cada palabra
    public void InsertQuery0() throws SQLException {
        String query = "update consulta_mrdd set frecuencia = 0 where query_id = 1";
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            //preparedStmt.setString(1, q.getTermino());
            //preparedStmt.setString(2, q.getRaiz());
            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception e) {
            //Do something
        } finally {
            conn.close();
        }
    }
    
    //Consultas----------------------------------------------------------
    public void SP(String str1, String str2, String tabla) throws SQLException {
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            CallableStatement cs = null;

            if (str1.equals("") && str2.equals("")) {
                if (tabla.equals("consulta")) {
                    cs = this.conn.prepareCall("{call restore()}");
                }
                if(tabla.equals("q")){
                    cs = this.conn.prepareCall("{call restore2()}");
                }
            } else {
                if (tabla.equals("consulta")) {
                    cs = this.conn.prepareCall("{call alterar('" + str1 + "','" + str2 + "')}");
                }
                if (tabla.equals("q")) {
                    cs = this.conn.prepareCall("{call alterar2('" + str1 + "','" + str2 + "')}");
                }
            }

            int i = cs.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
        }
    }
    
    //Ejecuta la stored procedure medida y regresa el resultado como un objeto de tipo Query; el resultado esta en 
    //el field con el mismo nombre de la medida
    public Query2 StoredProc3(String medida) throws SQLException {
        Query2 q = new Query2();

        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            CallableStatement cs = null;
            cs = this.conn.prepareCall("{call " + medida + "()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                q.setFrecuencia(rs.getFloat(medida));
                q.setUri(rs.getString("uri1"));
            }
            return q;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return q;
        } finally {
            conn.close();
        }
    }
    
    //Insertar una tupla a la tabla consulta por cada palabra que tenga la consulta. Todas se crean con 1 como uri
    //Para ejecutar este metodo, fue necesario parsear la consulta y crear un objeto de tipo Query por cada palabra,
    //y ejecutar este metodo por cada palabra
    public void InsertQuery(Query2 q) throws SQLException {
        String query = "insert into consulta (uri, raíz) values (?,?) on duplicate key update frecuencia = frecuencia + 1";
        try {
            conn = DriverManager.getConnection(myUrl, "root", "admin");
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, q.getUri());
            preparedStmt.setString(2, q.getRaiz());
            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception e) {
            //Do something
        } finally {
            conn.close();
        }
    }
    
    //Este método recupera las stopwords de la base de datos y las regresa en una List 
    public List<String> readDataBase2(String str) throws SQLException {

        List<String> list = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(myUrl, "root",
                    "admin");
            // the mysql select statement
            String query = str;

            // Statements allow to issue SQL queries to the database
            Statement statement = conn.createStatement();
            // Result set get the result of the SQL query
            ResultSet rs = statement.executeQuery(query);
            // writeResultSet(resultSet);	

            //Tomar las stopwords del ResultSet y ponerlos en una lista
            while (rs.next()) {
                String temp = rs.getString("words");
                list.add(temp);
            }
            return list;
        } catch (Exception e) {
            //System.err.println("Got an exception!");
            //System.err.println(e.getMessage());		
            return list;
        } finally {
            conn.close();
        }
    }
    
}
