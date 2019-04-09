package vn.com.cardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementValue {
    @JsonProperty("ELEMENT")
    String ELEMENT;

    public String getELEMENT() {
        return ELEMENT;
    }

    public void setELEMENT(String ELEMENT) {
        this.ELEMENT = ELEMENT;
    }
}
