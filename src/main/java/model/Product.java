package model;
import java.util.Objects;
public class Product {
	private int id;
    private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts;

    static double EXPIRATION_RATE = 0.60;

    public Product(String name) {
        this.name = name;
    }
    
    

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        this.id = ++totalProducts;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
        this.available = available;
        this.stock = stock;

    }
    public Product(int id, String name, int stock, Amount publicPrice){
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.publicPrice = publicPrice;
    }
    public Product( String name,boolean available, int stock, Amount publicPrice){
        
        this.name = name;
        this.stock = stock;
        this.publicPrice = publicPrice;
        this.available = available;
    }
    public Product(int id, String name,boolean available, int stock, Amount publicPrice){
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.publicPrice = publicPrice;
        this.available = available;
        this.wholesalerPrice = new Amount(publicPrice.getValue() / 2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    public void expire() {
        EXPIRATION_RATE = 0.4;
        this.getPublicPrice().setValue(
                this.getPublicPrice().getValue() * EXPIRATION_RATE);
    }

    @Override
    public String toString() {
        return "Product " + id + " - "+  name +" - "+ available +" - " + publicPrice + " - " +  stock + " - " +wholesalerPrice +'\n';
    }

   


}