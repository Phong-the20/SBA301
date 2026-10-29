package fu.edu.sba301.slot32.model;

public class Dimensions {
    private double widthCm;
    private double heightCm;

    public Dimensions() {}

    public Dimensions(double widthCm, double heightCm) {
        this.widthCm = widthCm;
        this.heightCm = heightCm;
    }

    public double getWidthCm() { return widthCm; }
    public void setWidthCm(double widthCm) { this.widthCm = widthCm; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    @Override
    public String toString() {
        return widthCm + "x" + heightCm + " cm";
    }
}
