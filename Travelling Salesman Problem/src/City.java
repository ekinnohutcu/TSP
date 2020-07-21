public class City {

    //Value of the city
    private int cityID;
    // x coordinate of the city
    private int x;
    // y coordinate of the city
    private int y;

    public City(int cityID, int x, int y) {
        this.cityID = cityID;
        this.x = x;
        this.y = y;
    }

    public int getCityID() {
        return cityID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceToCity(City city) {
        int x = Math.abs(getX() - city.getX());
        int y = Math.abs(getY() - city.getY());
        return (int) Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

}
