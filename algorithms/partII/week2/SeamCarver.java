import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;

    private double[][] energyTo;
    private int[][] xTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validateSeam(picture);
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateInt(x, width());
        validateInt(y, height());
        if (x < 0 || y < 0 || x >= this.picture.width() || y >= this.picture.height()) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == this.picture.width() - 1 || y == this.picture.height() - 1) {
            return 1000;
        }

        int[] colorXPre = getRGB(x - 1, y);
        int[] colorXNext = getRGB(x + 1, y);
        int xG = calc(colorXPre, colorXNext);

        int[] colorYPre = getRGB(x, y - 1);
        int[] colorYNext = getRGB(x, y + 1);
        int yG = calc(colorYPre, colorYNext);

        return Math.sqrt(xG + yG);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // Transpose picture.
        Picture original = picture;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.picture = transpose;

        // call findVerticalSeam
        int[] seam = findVerticalSeam();

        // Transpose back.
        this.picture = original;

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energyTo = new double[width()][height()];
        xTo = new int[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energyTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width(); x++) {
            energyTo[x][0] = 195075;
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                if (x > 0) {
                    relax(x, y, x - 1, y + 1);
                }

                relax(x, y, x, y + 1);

                if (x < width() - 1) {
                    relax(x, y, x + 1, y + 1);
                }
            }
        }

        // find minimum energy path
        double minEnergy = Double.POSITIVE_INFINITY;
        int minEnergyX = -1;
        for (int w = 0; w < width(); w++) {
            if (energyTo[w][height() - 1] < minEnergy) {
                minEnergyX = w;
                minEnergy = energyTo[w][height() - 1];
            }
        }
        assert minEnergyX != -1;

        int[] seam = new int[height()];
        seam[height() - 1] = minEnergyX;
        int prevX = xTo[minEnergyX][height() - 1];

        for (int h = height() - 2; h >= 0; h--) {
            seam[h] = prevX;
            prevX = xTo[prevX][h];
        }

        return seam;
    }

    private void relax(int x1, int y1, int x2, int y2) {
        if (energyTo[x2][y2] > energyTo[x1][y1] + energy(x2, y2)) {
            energyTo[x2][y2] = energyTo[x1][y1] + energy(x2, y2);
            xTo[x2][y2] = x1;
        }
    }
//    // sequence of indices for horizontal seam
//    public int[] findHorizontalSeam() {
//        return null;
//    }

//    // sequence of indices for vertical seam
//    public int[] findVerticalSeam() {
//        return null;
//    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam);
        if (height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(this.picture.width(), this.picture.height() - 1);

        for (int col = 0; col < picture.width(); col++) {
            if (seam[col] < 0 || seam[col] > picture.height() || (col > 0 && Math.abs(seam[col] - seam[col - 1]) > 1)) {
                throw new IllegalArgumentException();
            }

            int rowInx = 0;
            for (int row = 0; row < picture.height(); row++) {
                if (row != seam[col]) {
                    newPicture.set(col, rowInx++, this.picture.get(col, row));
                }
            }
        }

        this.picture = newPicture;
    }

    // remove vertical seam from current picture

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam);
        if (width() <= 1 || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(this.picture.width() - 1, this.picture.height());

        for (int row = 0; row < picture.height(); row++) {
            if (seam[row] < 0 || seam[row] > picture.width() || (row > 0 && Math.abs(seam[row] - seam[row - 1]) > 1)) {
                throw new IllegalArgumentException();
            }

            int colInx = 0;
            for (int col = 0; col < picture.width(); col++) {
                if (col != seam[row]) {
                    newPicture.set(colInx++, row, this.picture.get(col, row));
                }
            }
        }

        this.picture = newPicture;
    }

    private void validateInt(int v, int max) {
        if (v < 0 || v >= max) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSeam(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private int[] getRGB(int x, int y) {
        int color = this.picture.getRGB(x, y);

        //              from source code:
        //                | (getRed(inData) << 16)
        //                | (getGreen(inData) << 8)
        //                | (getBlue(inData) << 0)
        int blue = color & 0xff;
        int green = (color >> 8) & 0xff;
        int red = (color >> 16) & 0xff;
        return new int[]{red, green, blue};
    }

    private int calc(int[] a, int[] b) {
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result += Math.pow(a[i] - b[i], 2);
        }
        return result;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(3, 4);
        picture.set(0, 0, new Color(255, 101, 51));
        picture.set(0, 1, new Color(255, 153, 51));
        picture.set(0, 2, new Color(255, 203, 51));
        picture.set(0, 3, new Color(255, 255, 51));
        picture.set(1, 0, new Color(255, 101, 153));
        picture.set(1, 1, new Color(255, 153, 153));
        picture.set(1, 2, new Color(255, 204, 153));
        picture.set(1, 3, new Color(255, 255, 153));
        picture.set(2, 0, new Color(255, 101, 255));
        picture.set(2, 1, new Color(255, 153, 255));
        picture.set(2, 2, new Color(255, 205, 255));
        picture.set(2, 3, new Color(255, 155, 255));

        SeamCarver sc = new SeamCarver(picture);
        System.out.println(sc.energy(1, 1));
        System.out.println(sc.energy(1, 2));


        picture = new Picture("bin/production/algorithms/seam/diagonals.png");
        sc = new SeamCarver(picture);
        sc.removeHorizontalSeam(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        sc.picture().save("1.jpg");


        sc.removeHorizontalSeam(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        sc.picture().save("2.jpg");


        sc.removeHorizontalSeam(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        sc.picture().save("3.jpg");


        sc.removeHorizontalSeam(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        sc.picture().save("4.jpg");
    }
}
