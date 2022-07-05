package potracej;

public class Curve {
    public int n;                    /* number of segments */
    public CurveTag tag[];                 /* tag[n]: POTRACE_CURVETO or POTRACE_CORNER */
    public PointDouble c[][]; /* c[n][3]: control points. */

    public enum CurveTag {
        POTRACE_CURVETO, POTRACE_CORNER
    }
}
