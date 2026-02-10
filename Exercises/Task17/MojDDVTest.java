package Exercises.Task17;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum ItemType {
    A, B, V
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", amount));
    }
}

class Item {
    private int price;
    private ItemType type;

    public Item() {
    }

    public Item(int price, ItemType type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }


    public double getTax() {
        if (type == ItemType.A) {
            return price * 0.18 * 0.15;
        } else if (type == ItemType.B) {
            return price * 0.05 * 0.15;
        } else {
            return 0.0;
        }
    }
}

class Order {
    private String name;
    List<Item> items;

    public Order(String name, List<Item> items) {
        this.name = name;
        this.items = items;
    }

    public static Order createOrder(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        List<Item> items = new ArrayList<>();
        String name = parts[0];
        for (int i = 1; i < parts.length - 1; i += 2) {
            int price = Integer.parseInt(parts[i]);
            ItemType type = ItemType.valueOf(parts[i + 1]);
            items.add(new Item(price, type));
        }

        int sum = items.stream().mapToInt(Item::getPrice).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        return new Order(name, items);
    }

    public double getTaxReturns() {
        return items.stream().mapToDouble(Item::getTax).sum();
    }

    public String getName() {
        return name;
    }

    public int getSum() {
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", this.name, this.getSum(), this.getTaxReturns());
    }
}

class MojDDV {

    private List<Order> orders;

    public MojDDV() {
        orders = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        orders = reader.lines().map(line -> {
            try {
                return Order.createOrder(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        orders.forEach(order -> writer.println(order.toString()));
        writer.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        DoubleSummaryStatistics doubleSummaryStatistics = orders.stream()
                .mapToDouble(Order::getTaxReturns).summaryStatistics();
        writer.println(String.format("min:\t%05.03f\nmax:\t%05.03f\nsum:\t%05.03f\ncount:\t%-5d\navg:\t%05.03f",
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getMax(),
                doubleSummaryStatistics.getSum(),
                doubleSummaryStatistics.getCount(),
                doubleSummaryStatistics.getAverage()));

        writer.flush();

    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
