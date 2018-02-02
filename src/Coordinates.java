public class Coordinates {

    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXValue(){
        return this.x;
    }

    public int getYValue(){
        return this.y;
    }

    public double getDistance(Coordinates other) {
        int dx = this.x - other.getXValue();
        int dy = this.y - other.getYValue();

        double distance = Math.sqrt(dx*dx + dy*dy);
        return distance;
    }

}
