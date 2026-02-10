package Lab1.Task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {

    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

}

class MovablePoint implements Movable {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int newY = y + ySpeed;
        if (newY > MovablesCollection.getY_max()) {
            throw new ObjectCanNotBeMovedException(x, newY);
        }
        this.y = newY;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int newY = y - ySpeed;
        if (newY < 0) {
            throw new ObjectCanNotBeMovedException(x, newY);
        }
        this.y = newY;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int newX = x - xSpeed;
        if (newX < 0) {
            throw new ObjectCanNotBeMovedException(newX, y);
        }
        this.x = newX;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int newX = x + xSpeed;
        if (newX > MovablesCollection.getX_max()) {
            throw new ObjectCanNotBeMovedException(newX, y);
        }
        this.x = newX;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Lab1.Task2.Movable point with coordinates (%d,%d)", x, y);
    }
}

class MovableCircle implements Movable {
    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return String.format("Lab1.Task2.Movable circle with center coordinates (%d,%d) and radius %d",
                center.getCurrentXPosition(), center.getCurrentYPosition(), radius);
    }

}

class MovablesCollection {
    private List<Movable> movables;
    private static int x_max;
    private static int y_max;

    public MovablesCollection(int x_max, int y_max) {
        MovablesCollection.x_max = x_max;
        MovablesCollection.y_max = y_max;
        this.movables = new ArrayList<Movable>();
    }

    public static int getX_max() {
        return x_max;
    }

    public static int getY_max() {
        return y_max;
    }

    public static void setxMax(int xMax) {
        MovablesCollection.x_max = xMax;
    }

    public static void setyMax(int yMax) {
        MovablesCollection.y_max = yMax;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();

        if (m instanceof MovableCircle) {
            MovableCircle mc = (MovableCircle) m;
            int radius = mc.getRadius();
            if (x - radius < 0 || x + radius > x_max || y - radius < 0 || y + radius > y_max) {
                throw new MovableObjectNotFittableException(
                        String.format("Lab1.Task2.Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", x, y, radius)
                );
            }
        } else if (m instanceof MovablePoint) {
            if (x < 0 || x > x_max || y < 0 || y > y_max) {
                throw new MovableObjectNotFittableException(
                        String.format("Lab1.Task2.Movable point with coordinates (%d,%d) can not be fitted into the collection", x, y)
                );
            }
        }
        movables.add(m);
    }

    private void moveObject(Movable m, DIRECTION direction) throws ObjectCanNotBeMovedException {
        switch (direction) {
            case UP:
                m.moveUp();
                break;
            case DOWN:
                m.moveDown();
                break;
            case LEFT:
                m.moveLeft();
                break;
            case RIGHT:
                m.moveRight();
                break;
        }
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movables) {
            if (type == TYPE.POINT && m instanceof MovablePoint) {
                try {
                    moveObject(m, direction);
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            } else if (type == TYPE.CIRCLE && m instanceof MovableCircle) {
                try {
                    moveObject(m, direction);
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size " + movables.size() + ":\n");
        for (Movable m : movables) {
            sb.append(m.toString() + "\n");
        }
        return sb.toString();
    }

}


public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
