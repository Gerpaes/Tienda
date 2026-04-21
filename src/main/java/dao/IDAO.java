package dao;

import Exception.DAO_Excep;
import java.util.HashMap;
import java.util.List;
import model.Product;

/**
 *
 * @author fran
 * 
 */

public interface IDAO {
     
    public abstract HashMap<Integer,Product> readALL () throws DAO_Excep;
        
    public abstract HashMap<Integer,Product> read (Product product)throws DAO_Excep;
    
    public abstract HashMap<Integer,Product> readByDisponible (Product product)throws DAO_Excep;
    
    public abstract int insert(Product product) throws DAO_Excep;
    
    public abstract int update (Product product) throws DAO_Excep;
          
    public abstract int delete (Product product) throws DAO_Excep;
    
    public abstract int deleteALL () throws DAO_Excep;
    
//    public abstract int resetAges() throws DAO_Excep;
    
}
