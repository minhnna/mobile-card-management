package vn.com.cardmanagement.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A TopupRequest.
 */
@Document(collection = "topup_request")
public class TopupRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("mobile_service")
    private String mobileService;

    @Field("mobile_number")
    private String mobileNumber;

    @Field("topup_value")
    private Integer topupValue;

    @Field("real_value")
    private Integer realValue;

    @Field("created_date")
    private Instant createdDate;

    @Field("updated_date")
    private Instant updatedDate;

    @Field("user_id")
    private String userId;

    @Field("status")
    private String status;

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

    public TopupRequest mobileService(String mobileService) {
        this.mobileService = mobileService;
        return this;
    }

    public void setMobileService(String mobileService) {
        this.mobileService = mobileService;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public TopupRequest mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getTopupValue() {
        return topupValue;
    }

    public TopupRequest topupValue(Integer topupValue) {
        this.topupValue = topupValue;
        return this;
    }

    public void setTopupValue(Integer topupValue) {
        this.topupValue = topupValue;
    }

    public Integer getRealValue() {
        return realValue;
    }

    public TopupRequest realValue(Integer realValue) {
        this.realValue = realValue;
        return this;
    }

    public void setRealValue(Integer realValue) {
        this.realValue = realValue;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public TopupRequest createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public TopupRequest updatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserId() {
        return userId;
    }

    public TopupRequest userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public TopupRequest status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public TopupRequest deleted(Boolean deleted) {
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
        TopupRequest topupRequest = (TopupRequest) o;
        if (topupRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), topupRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TopupRequest{" +
            "id=" + getId() +
            ", mobileService='" + getMobileService() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", topupValue=" + getTopupValue() +
            ", realValue=" + getRealValue() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", userId='" + getUserId() + "'" +
            ", status='" + getStatus() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
