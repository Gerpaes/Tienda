package main;

import java.util.ArrayList;
import java.util.Locale;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;

public class Shop {

    private Amount cash = new Amount(100.00);
    private ArrayList<Product> inventory;
    private int numberProducts;
    private ArrayList<Sale> sales;
    private Amount totalAmountSales = new Amount(0);
    private int numberSales = 0;

    final static double TAX_RATE = 1.04;

    public Shop() {
        inventory = new ArrayList<>();
        sales = new ArrayList<>();
    }

    public static void main(String[] args) {

        Shop shop = new Shop();

        shop.loadInventory();

        Scanner scanner = new Scanner(System.in);
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
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;

                case 3:
                    shop.addStock();
                    break;

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

                case 9:
                    shop.deleteProduct();
                    break;

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
    public void loadInventory() {
        addProduct(new Product("Manzana", new Amount(5.00), true, 10));
        addProduct(new Product("Pera", new Amount(7.00), true, 20));
        addProduct(new Product("Hamburguesa", new Amount(8.00), true, 30));
        addProduct(new Product("Fresa", new Amount(9.00), true, 20));
    }

    /**
     * show current total cash
     */
    private void showCash() {
        System.out.println("Dinero actual: " + cash.getValue() + cash.getCurrency());

    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        Product product = findProduct(name);
        if (product == null) {
            System.out.println("Precio de venta");
            double publicPrice = scanner.nextDouble();
            System.out.print("Precio mayorista: ");
            double wholesalerPrice = scanner.nextDouble();
            System.out.print("Stock: ");
            int stock = scanner.nextInt();

            addProduct(new Product(name, new Amount(wholesalerPrice), true, stock));
            return;
        } else {
            System.out.println("The product alredy exist");
            return;
        }
    }

    /**
     * add stock for a specific product
     */
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            // ask for stock
            System.out.print("Seleccione la cantidad a a\u00f1adir: ");
            int stock = scanner.nextInt();
            // update stock product
            product.setStock(product.getStock() + stock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
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
        System.out.println("Contenido actual de la tienda: ");
        for (Product product : inventory) {
            if (product != null) {
                System.out.println(product.getName() + " " + product.getPublicPrice().getValue() + " " + product.getPublicPrice().getCurrency());
            }
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();
        System.out.println("Dime Id cliente");
        int memberid = sc.nextInt();
        
        Client cliente = new Client( client);

        // sale product until input name is not 0
        double totalAmount = 0.0;

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

                totalAmount += product.getPublicPrice().getValue();
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
        totalAmount = totalAmount * TAX_RATE;
        cash.setValue(cash.getValue() + totalAmount);

        totalAmountSales.setValue(totalAmountSales.getValue() + totalAmount);

        Sale sale = new Sale(new Client(client), null, new Amount(totalAmount));

        sale.setProducts(soldProducts);

        sales.add(sale);
        numberSales++;

        System.out.println("Venta realizada con exito, total: " + totalAmount + totalAmountSales.getCurrency());
    }

    /**
     * show all sales
     */
    private void showSales() {
        boolean empty = true;
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
                empty = false;
            }
        }
        if (empty) {
            System.out.println("La lista esta vacia");
        }
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {

        inventory.add(product);
        numberProducts++;
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
    public Product findProduct(String name) {
        for (Product producto : inventory) {
            if (inventory.contains(producto)) {
                return producto;
            }
        }
        return null;
    }

    public void showTotalAmountOfSales() {
        System.out.println(totalAmountSales.getValue() + totalAmountSales.getCurrency());
    }

    private void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        Product product = findProduct(name);
        if (product != null) {
            inventory.remove(product);
            System.out.println("Producto eliminado correctamente");

            
        } else {
            System.out.println("The product not exist");
            
        }
    }

}
