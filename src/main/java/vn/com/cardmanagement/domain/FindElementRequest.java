package vn.com.cardmanagement.domain;

public class FindElementRequest {
    String using = "id";
    String value;

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
