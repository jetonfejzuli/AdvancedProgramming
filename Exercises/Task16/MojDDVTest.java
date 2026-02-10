package Exercises.Task16;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum TYPE {
    A, B, V
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

class Item {
    private int price;
    private TYPE type;

    public Item() {
    }

    public Item(int price, TYPE type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public double getTax() {
        switch (type) {
            case A:
                return price * 0.18 * 0.15;
            case B:
                return price * 0.05 * 0.15;
            default:
                return 0;
        }
    }


    @Override
    public String toString() {
        return String.format("%d %s", price, type);
    }
}

class Account {
    private String id;
    List<Item> items;

    public Account(String id, List<Item> items) {
        this.id = id;
        int sum = items.stream().mapToInt(Item::getPrice).sum();
        this.items = items;
    }

    public static Account createItem(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        for (int i = 1; i < parts.length; i++) {
            if (i % 2 == 0) { //tax type
                item.setType(TYPE.valueOf(parts[i]));
                items.add(item);
                item = new Item();
            } else { //price
                item.setPrice(Integer.parseInt(parts[i]));
            }
        }
        int sum = items.stream().mapToInt(Item::getPrice).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        return new Account(id, items);
    }

    public double getTaxReturns() {
        return items.stream().mapToDouble(Item::getTax).sum();
    }

    public String getId() {
        return id;
    }

    public int getSum() {
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", id, this.getSum(), this.getTaxReturns());
    }

}


class MojDDV {

    private List<Account> accounts;

    public MojDDV() {
    }

    public MojDDV(List<Account> accounts) {
        this.accounts = accounts;
    }

    void readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        accounts = br.lines().map(line -> {
            try {
                return Account.createItem(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        accounts.forEach(account -> pw.println(account.toString()));
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}