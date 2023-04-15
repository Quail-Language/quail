package me.tapeline.quailj.preprocessing;

import java.util.ArrayList;
import java.util.List;

public class StringBoundariesMap {

    public static class Bounds {
        private int left;
        private int right;

        public Bounds(int left, int right) {
            this.left = left;
            this.right = right;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }
    }

    private List<Bounds> bounds = new ArrayList<>();

    public void addBoundary(int pos, int length) {
        bounds.add(new Bounds(pos, pos + length));
    }

    public boolean isInString(int pos) {
        for (Bounds bounds : this.bounds)
            if (bounds.getLeft() >= pos && bounds.getRight() < pos)
                return true;
        return false;
    }

    public boolean isStringBeginning(int pos) {
        for (Bounds bounds : this.bounds)
            if (bounds.getLeft() == pos)
                return true;
        return false;
    }

    public boolean isStringEnding(int pos) {
        for (Bounds bounds : this.bounds)
            if (bounds.getRight() - 1 == pos)
                return true;
        return false;
    }

}
