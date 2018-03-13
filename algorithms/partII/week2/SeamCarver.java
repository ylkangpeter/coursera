import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;

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
        validateInt(x, height());
        validateInt(y, width());
        if (x < 0 || y < 0 || x >= this.picture.width() || y >= this.picture.height()) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == this.picture.width() || y == this.picture.height()) {
            return 1000;
        }

        int[] color_x_pre = getRGB(x - 1, y);
        int[] color_x_next = getRGB(x + 1, y);
        int xG = calc(color_x_pre, color_x_next);

        int[] color_y_pre = getRGB(x, y - 1);
        int[] color_y_next = getRGB(x, y + 1);
        int yG = calc(color_y_pre, color_y_next);

        return Math.sqrt(xG + yG);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam);
        if (height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(this.picture.width(), this.picture.height() - 1);

        for (int col = 0; col < picture.width(); col++) {
            if (col > 0 && Math.abs(seam[col] - seam[col - 1]) > 1) {
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

        for (int row = 0; row < picture.width(); row++) {
            if (row > 0 && Math.abs(seam[row] - seam[row - 1]) != 1) {
                throw new IllegalArgumentException();
            }

            int colInx = 0;
            for (int col = 0; col < picture.height(); col++) {
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

    private void validateSeam(Object o) {
        if (o == null) {
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

        //
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
