import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiaData {
    
    @JsonProperty("tempmax")
    private double tempmax;
    
    @JsonProperty("tempmin")
    private double tempmin;
    
    public double getTempmax() {
        return tempmax;
    }
    
    public void setTempmax(double tempmax) {
        this.tempmax = tempmax;
    }
    
    public double getTempmin() {
        return tempmin;
    }
    
    public void setTempmin(double tempmin) {
        this.tempmin = tempmin;
    }
}