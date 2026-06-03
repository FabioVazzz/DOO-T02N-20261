public class WindInfo {

    public double speed;
    public double direction;

    public WindInfo(double speed, double direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public String formatted() {
        return Math.round(speed) + " km/h (" + Math.round(direction) + "°)";
    }
}