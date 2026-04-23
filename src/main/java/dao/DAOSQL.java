package dao;

import Exception.DAO_Excep;
import Exception.Read_SQL_DAO_Excep;
import Exception.Write_SQL_DAO_Excep;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Amount;
import model.Employee;
import model.Product;

/**
 *
 * @author Fran Perez
 */
public class DAOSQL implements IDAO {

    //Variables para la conexión segura contra el servidor (sin especificar DDBB)
    private final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private final String JDBC_COMMU_OPT = "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "";

    //Especificamos la base de Datos
    private final String JDBC_DDBB = "Tienda";
    private final String TABLE_PRODUCTOS = JDBC_DDBB + ".productos";
    private final String TABLE_EMPLEADOS = JDBC_DDBB + ".emplados";
//    private final String JDBC_DDBB_TABLE = JDBC_DDBB + "." + JDBC_TABLE;

    //Variables para las consultas SQL
    private final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_PRODUCTOS + ";";
    private final String SQL_SELECT = "SELECT * FROM " + TABLE_PRODUCTOS + " WHERE name = ?";
    private final String SQL_SELECT2 = "SELECT * FROM " + TABLE_EMPLEADOS + " WHERE (id = ";
    private final String SQL_INSERT = "INSERT INTO " + TABLE_PRODUCTOS+ " (name, disponible, stock, precio) VALUES (?, ?, ?, ?)";
    private final String SQL_UPDATE_STOCK = "UPDATE " + TABLE_PRODUCTOS+  " SET stock = ? WHERE name = ?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_EMPLEADOS + " WHERE name = ?";
    private final String SQL_DELETE_ALL = "DELETE FROM " + TABLE_PRODUCTOS+ ";";
//    private final String SQL_RESET_AGES = "UPDATE " + JDBC_DDBB_TABLE + " SET age = 0 WHERE (name = ?);";

    public Connection connect() throws DAO_Excep {
        Connection conn = null;
        try {
            //Esta línea no es necesaria, excepto en algunas aplicaciones WEB
            //En aplicaciones locales como esta no sería necesaria
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //getConnection necesita la BBDD, el usuario y la contraseña
            conn = DriverManager.getConnection(JDBC_URL + JDBC_COMMU_OPT, JDBC_USER, JDBC_PASSWORD);
            createDB(conn);
            createTable(conn);
//        } catch (ClassNotFoundException ex) {
//           ex.printStackTrace(System.out);
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not connect or create database with tables: " + JDBC_DDBB);
        }
        return conn;
    }

    private void createDB(Connection conn) throws SQLException {
        //Sentencia SQL que crea la BBDD si no existe en el servidor
        String instruction = "create database if not exists " + JDBC_DDBB + ";";
        Statement stmt = null;
        stmt = conn.createStatement();
        //La clase Statemen nos permite ejecutar sentencias SQL
        stmt.executeUpdate(instruction);
        //Liberamos los recursos de la comunicación   
        stmt.close();
    }

    private void createTable(Connection conn) throws SQLException {
        String query = "create table if not exists " + JDBC_DDBB + "." + TABLE_PRODUCTOS + "("
                + "id Bigint primary key auto_increment, "
                + "name varchar(70) unique, "
                + "disponible boolean, "
                + "stock int,"
                + "precio double);";
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeUpdate(query);

        //Liberamos los recursos de la comunicación  
//        PreparedStatement test = conn.prepareStatement(SQL_INSERT);
//        test.setString(1, "manzana");
//        test.setBoolean(2, true);
//        test.setInt(3, 10);
//        test.setDouble(4, 20.0);
//        test.addBatch();
//
//        // Producto 2 (opcional, para tener m�s datos)
//        test.setString(1, "pera");
//        test.setBoolean(2, true);
//        test.setInt(3, 5);
//        test.setDouble(4, 15.5);
//        test.addBatch();
        // Ejecutamos el lote
//        test.executeBatch();
        stmt.close();
    }

    private void createTableEmployee(Connection conn) throws SQLException {
        String query = "create table if not exists " + JDBC_DDBB + "." + TABLE_PRODUCTOS + "("
                + "id Bigint primary key auto_increment, "
                + "name varchar(70) unique, "
                + "disponible boolean, "
                + "stock int,"
                + "precio double);";
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeUpdate(query);

        stmt.close();
    }

    public void disconnect(Connection conn) throws DAO_Excep {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new DAO_Excep("Can not disconnect from database " + JDBC_DDBB);
            }
        }
    }

    @Override
    public HashMap<Integer, Product> readALL() throws DAO_Excep {
        HashMap<Integer, Product> products = new HashMap<Integer, Product>();
        Connection conn = null;
        Statement instruction = null;
        ResultSet rs = null;
        try {
            conn = connect();
            instruction = conn.createStatement();
            rs = instruction.executeQuery(SQL_SELECT_ALL);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("name");
                int stock = rs.getInt("stock");
                Amount precio = new Amount(rs.getDouble("precio"));
                boolean disponible = rs.getBoolean("disponible");

//                boolean adult =rs.getBoolean("adult");
                products.put(id, new Product(id, nombre, disponible, stock, precio));
            }
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not read from database - readAll");
        } finally {
            try {
                rs.close();
                instruction.close();
                disconnect(conn);
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not read from database - readAll");
            }
        }
        return products;
    }

    @Override
    public HashMap<Integer, Product> read(Product p) throws DAO_Excep {
        HashMap<Integer, Product> products = new HashMap<Integer, Product>();
        Product product = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = connect();
            ps = conn.prepareStatement(SQL_SELECT);
            ps.setString(1, p.getName());
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int stock = rs.getInt("stock");
                boolean disponible = rs.getBoolean("disponible");
                Amount precio = new Amount(rs.getDouble("precio"));
                product = new Product(id, name, disponible, stock, precio);
                products.put(id, product);
            }
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not read from database (DAO_COntroller.DAOSQL.read)");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    disconnect(conn);
                }

            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not close database read process (DAO_COntroller.DAOSQL.read)");
            }
        }
        return products;
    }

//    @Override
//    public HashMap<Integer, Product> readByName(Product p) throws DAO_Excep {
//        HashMap<Integer, Product> products = new HashMap<Integer, Product>();
//        Product product = null;
//        Connection conn = null;
//        Statement instruction = null;
//        ResultSet rs = null;
//        try {
//            conn = connect();
//            String query = SQL_SELECT2 + "'" + p.getname + "'" + ");";
//            System.out.println(query);
//            instruction = conn.createStatement();
//            rs = instruction.executeQuery(query);
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                int stock = rs.getInt("stock");
//                boolean disponible = rs.getBoolean("disponible");
//                Amount precio = new Amount(rs.getDouble("precio"));
//                product = new Product(id, name, disponible, stock, precio);
//                products.put(id, product);
//            }
//        } catch (SQLException ex) {
//            //ex.printStackTrace(System.out);
//            throw new Read_SQL_DAO_Excep("Can not read from database (DAO_COntroller.DAOSQL.read)");
//        } finally {
//            try {
//                rs.close();
//                instruction.close();
//                disconnect(conn);
//            } catch (SQLException ex) {
//                //ex.printStackTrace(System.out);
//                throw new Read_SQL_DAO_Excep("Can not close database read process (DAO_COntroller.DAOSQL.read)");
//            }
//        }
//        return products;
//    }
    @Override
    public int insert(Product product) throws DAO_Excep {
        Connection conn = null;
        //La clase PreparedStatement también permite ejecutar sentencias SQL
        //pero con mayor flexibilidad
        PreparedStatement instruction = null;
        int registers = 0;
        try {
            System.out.println("237i");
            conn = connect();
            instruction = conn.prepareStatement(SQL_INSERT);
            instruction.setString(1, product.getName());
            instruction.setBoolean(2, product.isAvailable());
            instruction.setInt(3, product.getStock());
            instruction.setDouble(4, product.getPublicPrice().getValue());
            registers = instruction.executeUpdate();
            System.out.println("245i");
        } catch (SQLException ex) {
            throw new DAO_Excep("Can not write to database (DAO_COntroller.DAOSQL.insert)");
        } finally {
            try {
                instruction.close();
                disconnect(conn);
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.insert)");
            }
        }
        //Devolvemos la cantidad de registros afectados, en nuestro caso siempre uno
        return registers;
    }

    @Override
    public boolean update(Product product) throws DAO_Excep {
        Connection conn = null;
        PreparedStatement instruction = null;
        boolean actualizado = false;
        try {
            conn = connect();
            instruction = conn.prepareStatement(SQL_UPDATE_STOCK);
            instruction.setInt(1, product.getStock());

            instruction.setString(2, product.getName());
            int filasAfec = instruction.executeUpdate();
            if (filasAfec > 0) {
                actualizado = true;
            }

        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not write to database (DAO_COntroller.DAOSQL.update)");
        } finally {
            try {
                if (instruction != null) {
                    instruction.close();
                }
                if (conn != null) {
                    disconnect(conn);
                }
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.update)");
            }
        }
        //Devolvemos la cantidad de registros afectados
        return actualizado;
    }

    @Override
    public boolean delete(Product product) throws DAO_Excep {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean actualizado = false;
        try {
            conn = connect();
            ps = conn.prepareStatement(SQL_DELETE);
            ps.setString(1, product.getName());
            //cada vez que modificamos una base de datos llamamos a executeUpdate()
            int registers = ps.executeUpdate();
            if (registers > 0) {
                actualizado = true;
            }
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not write to database (DAO_Controller.DAOSQL.delete)");

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    disconnect(conn);
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.delete)");
            }
        }
        //Devolvemos la cantidad de registros afectados
        return actualizado;
    }

    @Override
    public int deleteALL() throws DAO_Excep {
        Connection conn = null;
        PreparedStatement instruccion = null;
        int registers = 0;
        try {
            conn = connect();
            instruccion = conn.prepareStatement(SQL_DELETE_ALL);
            //cada vez que modificamos una base de datos llamamos a executeUpdate()
            registers = instruccion.executeUpdate();
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            throw new DAO_Excep("Can not write to database (DAO_Controller.DAOSQL.deleteAll)");
        } finally {
            try {
                instruccion.close();
                disconnect(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
                throw new DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.deleteAll)");
            }
        }
        //Devolvemos la cantidad de registros afectados
        return registers;
    }

//    @Override
//    public int resetAges() throws DAO_Excep {
//        //Esta operación se podría hacer con una única consulta SQL
//        //pero no lo hacemos así porque es un ejemplo de transacción
//        Connection conn = null;
//        PreparedStatement instruction = null;
//        int registers = 0;
//        try {
//            List<Student> students = readALL();
//            conn = connect();
//            conn.setAutoCommit(false);
//            if (!students.isEmpty()) {
//                for (Student a : students) {
//                    instruction = conn.prepareStatement(SQL_RESET_AGES);
//                    instruction.setString(1, a.getName());
//                    //cada vez que modificamos una base de datos llamamos a executeUpdate()
//                    registers += instruction.executeUpdate();
//                    //Activar para comprobar el funcionamiento del rollback
//                    //Debe haber más de un estudiante en la Base de datos (*)
    ////                    throw new SQLException();
//                }
//            }
//        } catch (SQLException ex) {
//            if (conn != null) {
//                try {
//                    conn.rollback();
//                } catch (SQLException ex1) {
//                    System.out.println("ROLLBACK");
//                }
//                //(*)
////                registers=0;
//            }
//        } finally {
//            try {
//                instruction.close();
//                conn.setAutoCommit(true);
//                disconnect(conn);
//            } catch (SQLException ex) {
//                ex.printStackTrace(System.out);
//                throw new Write_SQL_DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.deleteAll)");
//            }
//        }
//        //Devolvemos la cantidad de registros afectados
//        return registers;
//    }
    @Override
    public void login() throws DAO_Excep {

    }

    @Override
    public void logout() throws DAO_Excep {

    }

    @Override
    public Employee getEmployeeId(int employeeid, String password) throws DAO_Excep {
        Employee empl = null;
        return empl;
    }

}
