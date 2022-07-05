package potracej;

import java.util.ArrayList;

public class Path {
    public int area;       /* area of the bitmap path */
    public char sign;      /* '+' or '-', depending on orientation */
    public Curve curve;    /* this path's vector data */

    public Path next;      /* linked list structure */

    public Path childlist; /* tree structure */
    public Path sibling;   /* tree structure */

    PrivPath priv = new PrivPath();  /* private state */

    public Path(ArrayList<Point> pt, int area, char sign) {
        this.priv.pt = pt;
        this.area = area;
        this.sign = sign;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("path{(" + priv.pt.size() + " points):");
        for (Point Point : priv.pt) {
            sb.append(Point.toString() + " ");
        }
        sb.append(", priv=" + priv + "}");
        //sb.append(", \nnext="+next+"}");
        return sb.toString();
    }

}
