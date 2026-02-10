package Exercises.Task25;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde

class Product {
    private int discountPrice;
    private int price;

    public Product(int discountPrice, int price) {
        this.discountPrice = discountPrice;
        this.price = price;
    }

    public int discount() {
        return (int) ((price - discountPrice) * 100.0 / price);
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", discount(), discountPrice, price);
    }
}

class Store {
    private String name;
    private List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public int getAllDiscount() {
        return products.stream().mapToInt(p -> p.getPrice() - p.getDiscountPrice()).sum();
    }

    public double getAverageDiscount() {
        return products.stream().mapToDouble(Product::discount).sum() / products.size();
    }

    public static Store createStore(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String[] p = parts[i].split(":");
            int discountPrice = Integer.parseInt(p[0]);
            int price = Integer.parseInt(p[1]);
            products.add(new Product(discountPrice, price));
        }
        return new Store(name, products);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Average discount: ").append(String.format("%.1f", getAverageDiscount())).append("%\n");
        sb.append("Total discount: ").append(getAllDiscount()).append("\n");

        List<String> productStrings = products.stream()
                .sorted(Comparator.comparing(Product::discount)
                        .thenComparing(Product::getDiscountPrice).reversed())
                .map(Product::toString)
                .collect(Collectors.toList());

        for (int i = 0; i < productStrings.size(); i++) {
            sb.append(productStrings.get(i));
            if (i < productStrings.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}

class Discounts {
    private List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        stores = reader.lines().map(Store::createStore).collect(Collectors.toList());
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getAverageDiscount).reversed().thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getAllDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }


}