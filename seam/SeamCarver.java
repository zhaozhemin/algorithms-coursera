import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class SeamCarver {

    private int width;
    private int height;
    private int[][] pictureArray;
    private double[][] energyArray;
    private boolean transposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        buildPictureArray(picture);
        buildEnergyArray();
        transposed = false;
    }

    private void print2DArray(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int a = 0; a < arr[0].length; a++) {
                StdOut.print(arr[i][a]);
                StdOut.print("\t");
            }
            StdOut.println();
        }
    }

    private void buildPictureArray(Picture picture) {
        width = picture.width();
        height = picture.height();
        pictureArray = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pictureArray[row][col] = picture.getRGB(col, row);
            }
        }
    }

    private void buildEnergyArray() {
        energyArray = new double[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energyArray[row][col] = getEnergy(row, col);
            }
        }
    }

    private int coordToIndex(int row, int col) {
        return row * width + col;
    }

    private int[] indexToCoord(int i) {
        int row = i / width;
        int col = i % width;
        return new int[] { row, col };
    }

    private int[] indexToCoordTranposed(int i) {
        int row = i / height;
        int col = i % height;
        return new int[] { row, col };
    }

    private List<int[]> getAdjacentPixel(int row, int col) {
        List<int[]> coordList = new ArrayList<>();

        for (int i : new int[] { col - 1, col, col + 1 }) {
            if (validCoordinateRow(row + 1) && validCoordinateCol(i)) {
                coordList.add(new int[] { row + 1, i });
            }
        }

        return coordList;
    }

    private double getEnergy(int row, int col) {
        if (transposed && (row == 0 || row == width - 1 || col == 0 || col == height - 1)) {
            return 1000;
        }

        if (!transposed && (row == 0 || row == height - 1 || col == 0 || col == width - 1)) {
            return 1000;
        }

        int left = pictureArray[row - 1][col];
        int right = pictureArray[row + 1][col];
        double xGradient = Math.pow(getRed(right) - getRed(left), 2)
                + Math.pow(getGreen(right) - getGreen(left), 2)
                + Math.pow(getBlue(right) - getBlue(left), 2);

        int up = pictureArray[row][col - 1];
        int bottom = pictureArray[row][col + 1];
        double yGradient = Math.pow(getRed(bottom) - getRed(up), 2)
                + Math.pow(getGreen(bottom) - getGreen(up), 2)
                + Math.pow(getBlue(bottom) - getBlue(up), 2);

        return Math.sqrt(xGradient + yGradient);
    }

    private boolean validCoordinateCol(int col) {
        int up = transposed ? height : width;
        return col >= 0 && col < up;
    }

    private boolean validCoordinateColStrict(int col) {
        return col >= 0 && col < width;
    }

    private boolean validCoordinateRow(int row) {
        int up = transposed ? width : height;
        return row >= 0 && row < up;
    }

    private boolean validCoordinateRowStrict(int row) {
        return row >= 0 && row < height;
    }

    private int getBlue(int rgb) {
        return rgb & 255;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 255;
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 255;
    }

    // current picture
    public Picture picture() {
        if (transposed) {
            transpose();
        }

        Picture picture = new Picture(width, height);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                picture.setRGB(col, row, pictureArray[row][col]);
            }
        }

        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!validCoordinateColStrict(x) || !validCoordinateRowStrict(y)) {
            throw new IllegalArgumentException();
        }
        return transposed ? energyArray[x][y] : energyArray[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(true);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(false);
    }

    private int[] findSeam(boolean fromHorizontal) {
        if ((fromHorizontal && !transposed) || (!fromHorizontal && transposed)) {
            transpose();
        }

        SP sp = new SP(energyArray);
        int[] seam = new int[transposed ? width : height];
        int count = 0;

        for (int i : sp.shortestPathToBottom()) {
            seam[count] = transposed ? indexToCoordTranposed(i)[1] : indexToCoord(i)[1];
            count += 1;
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!transposed) {
            transpose();
        }

        if (seam == null || seam.length != width || height <= 1) {
            throw new IllegalArgumentException();
        }

        int prev = seam[0];

        for (int col = 0; col < seam.length; col++) {
            int row = seam[col];
            if (Math.abs(row - prev) > 1 || !validCoordinateRowStrict(row)) {
                throw new IllegalArgumentException();
            }
            prev = row;
            pictureArray[col] = shiftArray(pictureArray[col], row);
            energyArray[col] = shiftArray(energyArray[col], row);
        }

        height -= 1;

        for (int col = 0; col < seam.length; col++) {
            int row = seam[col];
            for (int[] coord : new int[][] {
                    { row - 1, col }, { row, col }
            }) {
                int r = coord[1];
                int c = coord[0];
                if (!validCoordinateRow(r) || !validCoordinateCol(c)) {
                    continue;
                }
                energyArray[r][c] = getEnergy(r, c);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (transposed) {
            transpose();
        }

        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException();
        }

        int prev = seam[0];

        for (int row = 0; row < seam.length; row++) {
            int col = seam[row];
            if (Math.abs(col - prev) > 1 || !validCoordinateColStrict(col)) {
                throw new IllegalArgumentException();
            }
            prev = col;
            pictureArray[row] = shiftArray(pictureArray[row], col);
            energyArray[row] = shiftArray(energyArray[row], col);
        }

        width -= 1;

        for (int row = 0; row < seam.length; row++) {
            int col = seam[row];
            for (int[] coord : new int[][] {
                    { row, col - 1 }, { row, col }
            }) {
                int r = coord[0];
                int c = coord[1];
                if (!validCoordinateRow(r) || !validCoordinateCol(c)) {
                    continue;
                }
                energyArray[r][c] = getEnergy(r, c);
            }
        }
    }

    private int[] shiftArray(int[] arr, int index) {
        int[] newArr = new int[arr.length - 1];
        System.arraycopy(arr, 0, newArr, 0, index);
        System.arraycopy(arr, index + 1, newArr, index, arr.length - index - 1);
        return newArr;
    }

    private double[] shiftArray(double[] arr, int index) {
        double[] newArr = new double[arr.length - 1];
        System.arraycopy(arr, 0, newArr, 0, index);
        System.arraycopy(arr, index + 1, newArr, index, arr.length - index - 1);
        return newArr;
    }

    private void transpose() {
        int[][] newPictureArray = new int[pictureArray[0].length][pictureArray.length];

        for (int i = 0; i < pictureArray.length; i++) {
            for (int a = 0; a < pictureArray[0].length; a++) {
                newPictureArray[a][i] = pictureArray[i][a];
            }
        }

        pictureArray = newPictureArray;

        double[][] newEnergyArray = new double[energyArray[0].length][energyArray.length];

        for (int i = 0; i < energyArray.length; i++) {
            for (int a = 0; a < energyArray[0].length; a++) {
                newEnergyArray[a][i] = energyArray[i][a];
            }
        }

        energyArray = newEnergyArray;
        transposed = !transposed;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }

}
