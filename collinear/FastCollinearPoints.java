import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private LineSegment[] lineSegments;
    private ArrayList<Point[]> bothEnds;

    public FastCollinearPoints(Point[] points) {
        checkArgument(points);

        this.points = points;
        bothEnds = new ArrayList<>();
        computeSegments();
        lineSegments = new LineSegment[bothEnds.size()];
        for (int i = 0; i < lineSegments.length; i++) {
            Point[] pair = bothEnds.get(i);
            lineSegments[i] = new LineSegment(pair[0], pair[1]);
        }
    }

    private void checkArgument(Point[] ps) {
        if (ps == null) {
            throw new IllegalArgumentException();
        }

        for (Point p : ps) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] pointsCopy = Arrays.copyOf(ps, ps.length);
        Arrays.sort(pointsCopy);

        for (int i = 0; i < pointsCopy.length; i++) {
            for (int j = i + 1; j < pointsCopy.length; j++) {
                if (pointsCopy[i].compareTo(pointsCopy[j]) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            }
        }
    }

    private boolean isSameSegment(Point a, Point b) {
        for (Point[] ps : bothEnds) {
            if (ps[0].compareTo(a) == 0 && ps[1].compareTo(b) == 0) {
                return true;
            }
        }
        return false;
    }

    private void computeSegments() {
        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(pointsCopy);
            Arrays.sort(pointsCopy, p.slopeOrder());
            int j = 0;

            while (j < pointsCopy.length) {
                int count = 0;
                Point startPoint = p.compareTo(pointsCopy[j]) < 0 ? p : pointsCopy[j];
                double slope = p.slopeTo(pointsCopy[j]);

                for (int k = j + 1; k < pointsCopy.length; k++) {
                    if (slope != p.slopeTo(pointsCopy[k])) {
                        break;
                    }
                    count += 1;
                }

                if (count >= 2) {
                    Point endPoint = p.compareTo(pointsCopy[j + count]) > 0 ? p :
                                     pointsCopy[j + count];
                    if (!isSameSegment(startPoint, endPoint)) {
                        bothEnds.add(new Point[] { startPoint, endPoint });
                    }
                    j = j + count + 1;
                    continue;
                }

                j += 1;
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    public static void main(String[] args) {

    }
}
