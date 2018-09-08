package vn.com.cardmanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Card.
 */
@Document(collection = "card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("mobile_service")
    private String mobileService;

    @Field("price")
    private Integer price;

    @Field("serial_number")
    private String serialNumber;

    @Field("code")
    private String code;

    @Field("created_date")
    private Instant createdDate;

    @Field("exported_date")
    private Instant exportedDate;

    @Field("updated_date")
    private Instant updatedDate;

    @Field("user_id")
    private String userId;

    @Field("status")
    private String status;

    @Field("real_price")
    private Integer realPrice;

    @Field("deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileService() {
        return mobileService;
    }

    public Card mobileService(String mobileService) {
        this.mobileService = mobileService;
        return this;
    }

    public void setMobileService(String mobileService) {
        this.mobileService = mobileService;
    }

    public Integer getPrice() {
        return price;
    }

    public Card price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Card serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCode() {
        return code;
    }

    public Card code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Card createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExportedDate() {
        return exportedDate;
    }

    public Card exportedDate(Instant exportedDate) {
        this.exportedDate = exportedDate;
        return this;
    }

    public void setExportedDate(Instant exportedDate) {
        this.exportedDate = exportedDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public Card updatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserId() {
        return userId;
    }

    public Card userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public Card status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRealPrice() {
        return realPrice;
    }

    public Card realPrice(Integer realPrice) {
        this.realPrice = realPrice;
        return this;
    }

    public void setRealPrice(Integer realPrice) {
        this.realPrice = realPrice;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Card deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        if (card.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), card.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", mobileService='" + getMobileService() + "'" +
            ", price=" + getPrice() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", code='" + getCode() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", exportedDate='" + getExportedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", userId='" + getUserId() + "'" +
            ", status='" + getStatus() + "'" +
            ", realPrice=" + getRealPrice() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
