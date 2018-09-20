package vn.com.cardmanagement.web.rest.params;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import vn.com.cardmanagement.config.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @JsonIgnore
    public void convertDateToMilliseconds(){
        if(!Strings.isNullOrEmpty(fromDate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                fromDate = String.valueOf((sdf.parse(fromDate)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!Strings.isNullOrEmpty(toDate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                toDate = String.valueOf((sdf.parse(toDate)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
