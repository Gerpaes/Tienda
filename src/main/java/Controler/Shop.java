package Controler;

import Exception.PersonasExcepcion;
import Exception.ProductExepcion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;
import model.Employee;
import model.Ficheros;

public class Shop {

    static private Amount cash = new Amount(100.00);
    static private HashMap<Integer,Product> inventory;
    static private int numberProducts;
    static private ArrayList<Sale> sales;
    static private Amount totalAmountSales = new Amount(0);
    static private int numberSales = 0;

    final static double TAX_RATE = 1.04;

    public Shop() {
        sales = new ArrayList<>();
        if (inventory == null) {
            inventory = new HashMap<Integer, Product>();
        } else {
            inventory.clear(); // Borramos lo que hubiera para no duplicar al recargar
        }

        sales = new ArrayList<>();

        // Ahora cargamos con seguridad
        model.Ficheros.LecturaFichero(inventory);

    }

    public static boolean initSesion(String nombre, int codigo, String contraseña) {
//        Scanner sca = new Scanner(System.in);
//        boolean logger = false;
//        while (!logger) {
//            System.out.println("El nombre del empleado");
//            String name = sca.nextLine();
//            System.out.println("Dime el id del empleado");
//            int user = sca.nextInt();
//            sca.nextLine();
//            System.out.println("Escribe la contraseña");
//            String password = sca.nextLine();
//            Employee employe = new Employee(nombre);
//
//            logger = employe.login(codigo, contraseña);
//            if (!logger) {
//               return logger;
//            
//            }
//        }
//              return !logger;
        Employee employe = new Employee(nombre);
        return employe.login(codigo, contraseña);
    }

    public static void cargarInventario() {
        // Aquí llamas a tu lógica de LecturaFichero

        // Como esta clase YA TIENE el array 'inventory', no hay pérdida
        Ficheros.LecturaFichero(inventory);
    }

    public static void main(String[] args) {

        Shop shop = new Shop();

        cargarInventario();

        Scanner scanner = new Scanner(System.in);
//        initSesion();
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n");
            System.out.println("===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) A\u00f1adir producto");
            System.out.println("3) A\u00f1adir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver total de ventas");
            System.out.println("9) Eliminar producto");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

//                case 2:
//                    shop.addProduct(nombre,  stock, new Amount(precio));
//                    break;
//                case 3:
//                    shop.addStock();
//                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;

                case 6:
                    shop.sale();
                    break;

                case 7:
                    shop.showSales();
                    break;

                case 8:
                    shop.showTotalAmountOfSales();
                    break;

//                case 9:
//                    shop.deleteProduct();
//                    break;

                case 10:
                    exit = true;
                    break;
                default:
                    System.out.println("El valor indicado no es valido.");
                    break;
            }
        } while (!exit);
    }

    /**
     * load initial inventory to shop
     */
//    public void loadInventory() {
//        addProduct(new Product("Manzana", new Amount(5.00), true, 10));
//        addProduct(new Product("Pera", new Amount(7.00), true, 20));
//        addProduct(new Product("Hamburguesa", new Amount(8.00), true, 30));
//        addProduct(new Product("Fresa", new Amount(9.00), true, 20));
//    }
    /**
     * show current total cash
     */
    public double showCash() {
        return cash.getValue();

    }

    /**
     * add a new product to inventory getting data from console
     */
    public boolean addProduct(String nombre, int stock, double precio) throws ProductExepcion {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Nombre: ");
//        String name = scanner.nextLine();
           int count  = 0 ;
        Product product = findProduct(nombre);
        if (product == null) {
//            System.out.print("Precio mayorista: ");
//            double wholesalerPrice = scanner.nextDouble();
//            System.out.print("Stock: ");
//            int stock = scanner.nextInt();

            addProduct(new Product(nombre, new Amount(precio), true, stock), count);
                count++;
            return true;
        } else {
            System.out.println("The product alredy exist");
            throw new ProductExepcion("El producto '" + nombre + "' ya existe.");

        }
    }

    /**
     * add stock for a specific product
     */
    public boolean addStock(String producto, int stock)throws ProductExepcion {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Seleccione un nombre de producto: ");
//        String name = scanner.next();
        Product product = findProduct(producto);

        if (product != null) {
            // ask for stock
//            System.out.print("Seleccione la cantidad a a\u00f1adir: ");
//            int stock = scanner.nextInt();
            // update stock product
            product.setStock(product.getStock() + stock);
            Ficheros.EscrituraFichero(inventory);
//            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());
            return true;
        } else {
            System.out.println("No se ha encontrado el producto con nombre " + producto);
            throw new ProductExepcion("El producto no existe");
        }
    }

    /**
     * set a product as expired
     */
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();

        Product product = findProduct(name);

        if (product != null) {
            product.expire();
            System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());

        }
    }

    /**
     * show all inventory
     */
    public void showInventory() {

        inventory.clear();
        Ficheros.LecturaFichero(inventory);

        System.out.println("Contenido actual de la tienda:");
        System.out.println("--------------------------------");

        Iterator it = inventory.keySet().iterator();
        Product key = (Product)it.next();

            System.out.println(
                    key.getName() + " | Precio: "
                    + key.getPublicPrice().getValue() + " ?"
                    + " | Stock: " + key.getStock()
            );

        }

//    System.out.println("Contenido actual de la tienda:");
//    System.out.println("--------------------------------");
//
//    for (Product product : inventory) {
//        System.out.println(
//            product.getName() + " | Precio: " +
//            product.getWholesalerPrice().getValue() +
//            " | Stock: " + product.getStock()
//        );
//        System.out.println("Contenido actual de la tienda: ");
//        for (Product product : inventory) {
//            if (product != null) {
//                System.out.println(product.getName() + " " + product.getPublicPrice().getValue() + " " + product.getPublicPrice().getCurrency());
//            }
//        }
    

    /**
     * make a sale of products to a client
     */
    public static void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();
        System.out.println("Dime Id cliente");
        int memberid = sc.nextInt();
        sc.nextLine();

        Client cliente = new Client(client);

        // sale product until input name is not 0
        Amount totalAmount = new Amount(0);

        ArrayList<Product> soldProducts = new ArrayList<>();
        int soldCount = 0;

        String name = "";
        while (!name.equals("0")) {

            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }

            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                productAvailable = true;

                soldProducts.add(product);
                soldCount++;

                totalAmount.setValue(
                        totalAmount.getValue() + product.getPublicPrice().getValue());
                product.setStock(product.getStock() - 1);

                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }

                System.out.println("Producto añadido con éxito");
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }
    

        // show cost total
        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
        cash.setValue(cash.getValue() + totalAmount.getValue());
        totalAmountSales.setValue(
                totalAmountSales.getValue() + totalAmount.getValue());

        boolean paid = cliente.pay(totalAmount);

        double deuda = totalAmount.getValue() - 50.00;
        if (!paid) {
            System.out.println(
                    "Saldo insuficiente. El cliente debe: "
                    + String.format("%.2f ?", deuda)
            );
        }

        Sale sale = new Sale(cliente, null, totalAmount);

        sale.setProducts(soldProducts);
        sales.add(sale);
        numberSales++;

        System.out.println("Venta realizada con exito, total: " + totalAmount + totalAmountSales.getCurrency() + " " + deuda);
    }

    /**
     * show all sales
     */
    private static void showSales() {
        Scanner sc = new Scanner(System.in);
        boolean empty = true;
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
                empty = false;
            }
        }
        System.out.println("Desea escribir las ventas en un fichero?");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("si")) {
            Ficheros.writeSales(sales);
        } else if (respuesta.equalsIgnoreCase("no")) {

            if (empty) {
                System.out.println("La lista esta vacia");
            }
        } else {
            System.out.println("Opcion no correcta");
        }

    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public static void addProduct(Product product, Integer n) {
        inventory.put(1,product);
        numberProducts++;
        Ficheros.EscrituraFichero(inventory);
    }

    /**
     * check if inventory is full or not
     *
     * @return true if inventory is full
     */
//    public boolean isInventoryFull() {
//        if (numberProducts == 10) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public static Product findProduct(String name) {
        Iterator it = inventory.keySet().iterator();
        
        while(it.hasNext()){
             Product key = (Product)it.next();
            if (key.getName().equalsIgnoreCase(name)) {
                return key;
        }
           
            }
            return null;
        }
        
    

    public void showTotalAmountOfSales() {
        System.out.println(totalAmountSales.getValue() + totalAmountSales.getCurrency());
    }

    public boolean deleteProduct( String producto) throws ProductExepcion{
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Nombre: ");
//        String name = scanner.nextLine();
        Product product = findProduct(producto);
        if (product != null) {
            inventory.remove(product);
            Ficheros.EscrituraFichero(inventory);
            System.out.println("Producto eliminado correctamente");
            return true;

        } else {
          
            System.out.println("The product not exist");
            throw new   ProductExepcion("El producto no existe");

        }
    }
}


