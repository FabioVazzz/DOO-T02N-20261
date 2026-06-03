import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaApi {
    
    @JsonProperty("resolvedAddress")
    private String resolvedAddress;
    
    @JsonProperty("currentConditions")
    private CondicaoAtual currentConditions;
    
    @JsonProperty("days")
    private DiaData[] days;
    
    public String getResolvedAddress() {
        return resolvedAddress;
    }
    
    public void setResolvedAddress(String resolvedAddress) {
        this.resolvedAddress = resolvedAddress;
    }
    
    public CondicaoAtual getCurrentConditions() {
        return currentConditions;
    }
    
    public void setCurrentConditions(CondicaoAtual currentConditions) {
        this.currentConditions = currentConditions;
    }
    
    public DiaData[] getDays() {
        return days;
    }
    
    public void setDays(DiaData[] days) {
        this.days = days;
    }
}