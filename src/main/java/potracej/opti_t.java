package potracej;

public class opti_t {
    double pen;       /* penalty */
    PointDouble c[];   /* curve parameters */
    double t, s;	   /* curve parameters */
    double alpha;	   /* curve parameter */

    public opti_t() {
        c = new PointDouble[2];
        c[0] = new PointDouble();
        c[1] = new PointDouble();
    }
}
