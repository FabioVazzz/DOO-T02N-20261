import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CondicaoAtual {
    
    @JsonProperty("temp")
    private double temp;
    
    @JsonProperty("humidity")
    private int humidity;
    
    @JsonProperty("conditions")
    private String conditions;
    
    @JsonProperty("precip")
    private double precip;
    
    @JsonProperty("windspeed")
    private double windspeed;
    
    @JsonProperty("winddir")
    private int winddir;
    
    @JsonProperty("datetimeEpoch")
    private long datetimeEpoch;
    
    public double getTemp() {
        return temp;
    }
    
    public void setTemp(double temp) {
        this.temp = temp;
    }
    
    public int getHumidity() {
        return humidity;
    }
    
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    
    public String getConditions() {
        return conditions;
    }
    
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    
    public double getPrecip() {
        return precip;
    }
    
    public void setPrecip(double precip) {
        this.precip = precip;
    }
    
    public double getWindspeed() {
        return windspeed;
    }
    
    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }
    
    public int getWinddir() {
        return winddir;
    }
    
    public void setWinddir(int winddir) {
        this.winddir = winddir;
    }
    
    public long getDatetimeEpoch() {
        return datetimeEpoch;
    }
    
    public void setDatetimeEpoch(long datetimeEpoch) {
        this.datetimeEpoch = datetimeEpoch;
    }
}