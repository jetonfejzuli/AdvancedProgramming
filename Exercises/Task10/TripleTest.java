package Exercises.Task10;

import java.util.Scanner;

class Triple<T extends Number> {
    T a;
    T b;
    T c;

    public Triple(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    double max() {
        double m = Math.max(a.doubleValue(), b.doubleValue());
        return Math.max(m, c.doubleValue());
    }

    double average() {
        return (a.doubleValue() + b.doubleValue() + c.doubleValue()) / 3.0;
    }

    void sort() {
        if (a.doubleValue() > b.doubleValue()) {
            T tmp = b;
            b = a;
            a = tmp;
        }
        if (b.doubleValue() > c.doubleValue()) {
            T tmp = c;
            c = b;
            b = tmp;
        }
        if (a.doubleValue() > b.doubleValue()) {
            T tmp = b;
            b = a;
            a = tmp;
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", a.doubleValue(), b.doubleValue(), c.doubleValue());
    }


}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Exercises.Task10.Triple


