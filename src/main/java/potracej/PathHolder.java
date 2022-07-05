package potracej;

public class PathHolder {
    Path value;
    Path last;

    public PathHolder(Path value) {
        this.value = value;
    }

    public void addLast(Path path) {
        if (value == null) {
            value = last = path;
        } else {
            last.next = path;
            last = path;
        }
        path.next = null;
    }

    public Path getValue() {
        return value;
    }

    public Path getLast() {
        return last;
    }
}
