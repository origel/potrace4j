package potracej;

public class Point {
    public int x;
    public int y;


    public Point(Point o) {
        this.x = o.x;
        this.y = o.y;
    }


    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "{"+x+","+y+"}";
    }
}
