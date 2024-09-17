package nz.ac.myunitec.cs.assignment1;

public class CityData {
    private String cityName;
    private Integer cityImage;

    public CityData(String cityName, Integer cityImage) {
        this.cityName = cityName;
        this.cityImage = cityImage;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityImage() {
        return cityImage;
    }

    public void setCityImage(Integer cityImage) {
        this.cityImage = cityImage;
    }
}
