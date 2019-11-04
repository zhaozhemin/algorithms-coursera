import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = put(root, p, true, false, null);
    }

    private Node put(Node n, Point2D p, boolean orientation, boolean lb, Node parent) {
        if (n == null) {
            size += 1;
            RectHV rect = calculateRect(parent, orientation, lb);
            return new Node(p, rect, null, null);
        }

        if (n.p.compareTo(p) == 0) {
            return n;
        }

        double nField = orientation ? n.p.x() : n.p.y();
        double pField = orientation ? p.x() : p.y();


        if (pField < nField) {
            n.lb = put(n.lb, p, !orientation, true, n);
        }
        else {
            n.rt = put(n.rt, p, !orientation, false, n);
        }

        return n;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return get(root, p, true) != null;
    }

    private Node get(Node n, Point2D p, boolean orientation) {
        if (n == null) {
            return null;
        }

        if (n.p.compareTo(p) == 0) {
            return n;
        }

        double nField = orientation ? n.p.x() : n.p.y();
        double pField = orientation ? p.x() : p.y();

        if (pField < nField) {
            return get(n.lb, p, !orientation);
        }
        else {
            return get(n.rt, p, !orientation);
        }
    }

    private RectHV calculateRect(Node parent, boolean orientation, boolean lb) {
        if (parent == null) {
            return new RectHV(0, 0, 1, 1);
        }

        RectHV pRect = parent.rect;
        Point2D pPoint = parent.p;

        if (!orientation) {
            if (lb) {
                return new RectHV(pRect.xmin(), pRect.ymin(), pPoint.x(), pRect.ymax());
            }
            else {
                return new RectHV(pPoint.x(), pRect.ymin(), pRect.xmax(), pRect.ymax());
            }
        }
        else {
            if (lb) {
                return new RectHV(pRect.xmin(), pRect.ymin(), pRect.xmax(), pPoint.y());
            }
            else {
                return new RectHV(pRect.xmin(), pPoint.y(), pRect.xmax(), pRect.ymax());
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) {
            return;
        }

        n.p.draw();
        draw(n.lb);
        draw(n.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return searchRange(root, rect);
    }

    private List<Point2D> searchRange(Node n, RectHV rect) {
        if (n == null) {
            return new ArrayList<>();
        }

        ArrayList<Point2D> inRange = new ArrayList<>();

        if (rect.contains(n.p)) {
            inRange.add(n.p);
        }

        if (n.lb != null && rect.intersects(n.lb.rect)) {
            inRange.addAll(searchRange(n.lb, rect));
        }

        if (n.rt != null && rect.intersects(n.rt.rect)) {
            inRange.addAll(searchRange(n.rt, rect));
        }

        return inRange;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return searchNearest(root, p, root != null ? root.p : null);
    }

    private Point2D searchNearest(Node n, Point2D p, Point2D closest) {
        if (n == null) {
            return null;
        }

        // StdOut.println("- " + n.p);

        if (n.p.distanceTo(p) < closest.distanceTo(p)) {
            closest = n.p;
        }

        double closestDist = closest.distanceTo(p);
        Point2D leftClosest = closest;
        Point2D rightClosest = closest;

        // Don't check any nodes
        if (n.lb == null && n.rt == null) {
            ;
        }
        // Only check right node
        else if (n.rt != null
                && n.rt.rect.distanceTo(p) < closestDist
                && (n.lb == null || n.lb.rect.distanceTo(p) >= closestDist)) {
            rightClosest = searchNearest(n.rt, p, closest);
        }
        // Only check left node
        else if (n.lb != null
                && n.lb.rect.distanceTo(p) < closestDist
                && (n.rt == null || n.rt.rect.distanceTo(p) >= closestDist)) {
            leftClosest = searchNearest(n.lb, p, closest);
        }
        // Query point lies in the left node, so check left first
        else if (n.lb != null
                && n.lb.rect.distanceTo(p) < closestDist
                && n.rt != null
                && n.rt.rect.distanceTo(p) < closestDist
                && n.lb.rect.distanceTo(p) < n.rt.rect.distanceTo(p)) {
            leftClosest = searchNearest(n.lb, p, closest);

            if (leftClosest.distanceTo(p) < closestDist) {
                closest = leftClosest;
            }

            if (leftClosest.distanceTo(p) > n.rt.rect.distanceTo(p)) {
                rightClosest = searchNearest(n.rt, p, closest);
            }
        }
        else if (n.lb != null
                && n.lb.rect.distanceTo(p) < closestDist
                && n.rt != null
                && n.rt.rect.distanceTo(p) < closestDist
                && n.rt.rect.distanceTo(p) <= n.lb.rect.distanceTo(p)) {
            rightClosest = searchNearest(n.rt, p, closest);

            if (rightClosest.distanceTo(p) < closestDist) {
                closest = rightClosest;
            }

            if (rightClosest.distanceTo(p) > n.lb.rect.distanceTo(p)) {
                leftClosest = searchNearest(n.lb, p, closest);
            }
        }

        Point2D[] candidates = new Point2D[] { closest, leftClosest, rightClosest };
        Arrays.sort(candidates, p.distanceToOrder());
        return candidates[0];
    }

    public static void main(String[] args) {

    }
}
