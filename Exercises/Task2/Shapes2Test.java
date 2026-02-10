package Exercises.Task2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum Type {
    C, S
}

class InvalidCanvasException extends Exception {

    public InvalidCanvasException(String id, double maxArea) {
        super(String.format("Exercises.Task2.Canvas %s has a shape with area larger than %.2f", id, maxArea));
    }

}

abstract class Shape {
    protected int size;

    public Shape(int size) {
        this.size = size;
    }

    abstract double area();

    abstract Type type();

    public static Shape createShape(int size, char type, double maxArea) {
        switch (type) {
            case 'C':
                return new Circle(size);
            case 'S':
                return new Square(size);
            default:
                return null;
        }
    }

}


class Circle extends Shape {

    public Circle(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size * Math.PI;
    }

    @Override
    public Type type() {
        return Type.C;
    }

}

class Square extends Shape {
    public Square(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size;
    }

    @Override
    public Type type() {
        return Type.S;
    }
}

class Canvas implements Comparable<Canvas> {
    private String id;
    private List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public static Canvas createCanvas(String line, double maxArea) throws InvalidCanvasException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            Shape s = Shape.createShape(Integer.parseInt(parts[i + 1]), parts[i].charAt(0), maxArea);
            if (s.area() > maxArea) {
                throw new InvalidCanvasException(id, maxArea);
            }
            shapes.add(Shape.createShape(Integer.parseInt(parts[i + 1]), parts[i].charAt(0), maxArea));
        }
        return new Canvas(id, shapes);
    }


    @Override
    public int compareTo(Canvas other) {
        return Double.compare(this.shapes.stream()
                .mapToDouble(Shape::area)
                .sum(), other.shapes.stream()
                .mapToDouble(Shape::area).sum());
    }

    int getCirclesCount() {
        return (int) shapes.stream().filter(i -> i.type().equals(Type.C)).count();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::area).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                shapes.size(),
                getCirclesCount(),
                shapes.size() - getCirclesCount(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }

}

class ShapesApplication {
    private List<Canvas> canvases;
    private double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvases = new ArrayList<>();
    }

    public void readCanvases(InputStream inputStream) throws IOException {
        canvases = new BufferedReader(new InputStreamReader(inputStream))
                .lines().map(line -> {
                    try {
                        return Canvas.createCanvas(line, maxArea);
                    } catch (InvalidCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void printCanvases(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        canvases.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }
}


public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}