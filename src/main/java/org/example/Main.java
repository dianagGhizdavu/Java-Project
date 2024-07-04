package org.example;

import org.example.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


import static org.example.DBService.findByEmail;

public class Main {
    public static List<Product> products = new ArrayList<>();
    public static Cart cart = new Cart();
    private static User currentUser = null;
    private static DBService dbService = new DBService();


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

//        System.out.println(new DBService().getAllUser());

//show main menu
        initializeProducts();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    showProductsMenu(scanner);
                    break;
                case 2:
                    viewCart();
                    break;
                case 3:
                    login(scanner);
                    break;
                case 4:
                    register(scanner);
                    break;
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initializeProducts() {
        products.add(new TV("Samsung Smart TV", 800, "103 cm 4K UHD"));
        products.add(new TV("LG OLED TV", 899, "129 cm OLED"));
        products.add(new Smartphone("iPhone 15 Pro", 999, "128GB Storage"));
        products.add(new Smartphone("Samsung Galaxy S21", 799, "256GB Storage"));
    }

    private static void showMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. View all products");
        System.out.println("2. View my cart");
        System.out.println("3. Login");
        System.out.println("4. Register");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private static void showProductsMenu(Scanner scanner) {
        System.out.println("\nProducts Menu:");
        System.out.println("1. TV");
        System.out.println("2. Smartphones");
        System.out.println("3. Back to the main menu");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                showProductSubMenu(scanner, "TV");
                break;
            case 2:
                showProductSubMenu(scanner, "Smartphone");
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void showProductSubMenu(Scanner scanner, String category) {
        List<Product> categoryProducts = new ArrayList<>();
        for (Product product : products) {
            if ((category.equals("TV") && product instanceof TV) || (category.equals("Smartphone") && product instanceof Smartphone)) {
                categoryProducts.add(product);
            }
        }

        for (int i = 0; i < categoryProducts.size(); i++) {
            System.out.println((i + 1) + ". " + categoryProducts.get(i).getName());
        }
        System.out.println((categoryProducts.size() + 1) + ". Back to the products menu");
        System.out.print("Select a product: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (choice >= 1 && choice <= categoryProducts.size()) {
            showProductOptions(scanner, categoryProducts.get(choice - 1));
        }
    }

    private static void showProductOptions(Scanner scanner, Product product) {
        while (true) {
            System.out.println("\nProduct Options:");
            System.out.println("1. Add to the cart");
            System.out.println("2. View details");
            System.out.println("3. Back to the products menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    cart.addProduct(product);
                    System.out.println(product.getName() + " has been added to the cart.");
                    System.out.println("1. Purchase now");
                    System.out.println("2. Exit");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (subChoice == 1) {
                        if (currentUser != null) {
                            System.out.println("Thank you for your purchase!");
                            return;
                        } else {
                            System.out.println("You need to be logged in to purchase.");
                            return;
                        }
                    } else if (subChoice == 2) {
                        return;
                    }
                    break;
                case 2:
                    System.out.println("\nProduct Details:");
                    System.out.println("Name: " + product.getName());
                    System.out.println("Price: $" + product.getPrice());
                    System.out.println("Details: " + product.getDetails());
                    System.out.println("1. Add to the cart");
                    System.out.println("2. Back to the products menu");
                    int detailChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (detailChoice == 1) {
                        cart.addProduct(product);
                        System.out.println(product.getName() + " has been added to the cart.");
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewCart() {
        if (currentUser == null) {
            System.out.println("You need to be logged in to view the cart.");
            return;
        }

        List<Product> cartProducts = cart.getProducts();
        if (cartProducts.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour Cart:");
            for (Product product : cartProducts) {
                System.out.println("- " + product.getName() + " ($" + product.getPrice() + ")");
            }
        }
    }

//login method and find user in db by their email
public static void login(Scanner scanner) {
    System.out.print("Enter email: ");
    String email = scanner.nextLine();
    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    if (dbService.loginUser(email, password)) {
        currentUser = findByEmail(email);
        System.out.println("Login successful! Welcome, " + currentUser.getUsername());
    } else {
        System.out.println("Login failed. Please check your email and password.");
    }
}

// register and save users in db
    private static void register(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!isValidEmail(email)) {
            System.out.println("Invalid email. Please try again.");
            return;
        }

        currentUser = new User(username, email, password);
        dbService.save(username, email, password);
        System.out.println("Registration successful. You can now log in.");
    }

    //email validation - should contain '@'
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
