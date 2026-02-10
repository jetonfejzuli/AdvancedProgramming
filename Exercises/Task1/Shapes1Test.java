package Exercises.Task1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Canvass {
    private String id;
    private List<Integer> sizes;

    public Canvass(String id) {
        this.id = id;
        this.sizes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public void add(int size) {
        sizes.add(size);
    }

    public int getPerimeter() {
        return sizes.stream().mapToInt(i -> 4 * i).sum();
    }

    public int getCount() {
        return sizes.size();
    }


}

class ShapeApplication {
    List<Canvass> canvas = new ArrayList<>();

    public ShapeApplication() {
        this.canvas = new ArrayList<>();
    }

    int readCanvases(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            count += parts.length - 1;
            Canvass canvas = new Canvass(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                canvas.add(Integer.parseInt(parts[i]));
            }
            this.canvas.add(canvas);
        }
        return count;
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        Canvass c = canvas.stream().max(Comparator.comparingInt(Canvass::getPerimeter)).get();
        pw.println(String.format("%s %d %d", c.getId(), c.getCount(), c.getPerimeter()));
        pw.flush();
    }

}


public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapeApplication shapesApplication = new ShapeApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}