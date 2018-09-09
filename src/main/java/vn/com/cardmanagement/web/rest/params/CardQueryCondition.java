package vn.com.cardmanagement.web.rest.params;


import vn.com.cardmanagement.config.Constants;

public class CardQueryCondition {

	/**
     * Filter image by checker. Value: Human, AI, All
     */
    private Constants.MobileService mobileService;

    private int price;

    private Constants.Status status;
    /**
     * User Id
     */
    private int quantity;

    private String fromDate;

    private String toDate;

    private String userId;

    public Constants.MobileService getMobileService() {
        return mobileService;
    }

    public void setMobileService(Constants.MobileService mobileService) {
        this.mobileService = mobileService;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Constants.Status getStatus() {
        return status;
    }

    public void setStatus(Constants.Status status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
