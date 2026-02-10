package Lab3.Task2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class Ad implements Comparable<Ad> {
    private String id;
    private String category;
    private double bidValue;
    private double ctr;
    private String content;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    @Override
    public String toString() {
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s", id, category, bidValue, ctr * 100, content);
    }

    @Override
    public int compareTo(Ad other) {
        if (this.bidValue == other.bidValue) {
            return this.id.compareTo(other.id);
        }
        return Double.compare(other.bidValue, this.bidValue);
    }

}

class AdRequest {
    private String id;
    private String category;
    private double floorBid;
    private String keywords;

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (%.2f): %s", id, category, floorBid, keywords);
    }

}

class AdNetwork {
    private List<Ad> ads;

    public AdNetwork() {
        this.ads = new ArrayList<>();
    }

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }


    public void readAds(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            String[] parts = line.split("\\s+");
            ads.add(new Ad(
                    parts[0],
                    parts[1],
                    Double.parseDouble(parts[2]),
                    Double.parseDouble(parts[3]),
                    String.join(" ", Arrays.copyOfRange(parts, 4, parts.length))
            ));
        }
    }


    public List<Ad> placeAds(BufferedReader reader, int k, PrintWriter writer) throws IOException {
        double x = 5.0, y = 100.0;

        String[] parts = reader.readLine().split("\\s+");
        AdRequest adRequest = new AdRequest(
                parts[0],
                parts[1],
                Double.parseDouble(parts[2]),
                String.join(" ", Arrays.copyOfRange(parts, 3, parts.length))
        );


        List<Ad> result = ads.stream()
                .filter(ad -> ad.getBidValue() >= adRequest.getFloorBid())
                .sorted((a, b) -> {
                    double scoreA = relevanceScore(a, adRequest) + x * a.getBidValue() + y * a.getCtr();
                    double scoreB = relevanceScore(b, adRequest) + x * b.getBidValue() + y * b.getCtr();
                    return Double.compare(scoreB, scoreA);
                })
                .limit(k)
                .sorted()
                .collect(Collectors.toList());

        writer.printf("Top ads for request %s:\n", adRequest.getId());
        result.forEach(writer::println);
        writer.flush();

        return result;

    }

}


public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}
