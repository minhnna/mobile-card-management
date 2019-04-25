package vn.com.cardmanagement.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TopupRequest entity.
 */
public class TopupRequestDTO implements Serializable {

    private String id;

    private String mobileService;

    private String mobileNumber;

    private Integer topupValue;

    private Integer realValue;

    private Instant createdDate;

    private Instant updatedDate;

    private String userId;

    private String status;

    private Boolean deleted;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileService() {
        return mobileService;
    }

    public void setMobileService(String mobileService) {
        this.mobileService = mobileService;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getTopupValue() {
        return topupValue;
    }

    public void setTopupValue(Integer topupValue) {
        this.topupValue = topupValue;
    }

    public Integer getRealValue() {
        return realValue;
    }

    public void setRealValue(Integer realValue) {
        this.realValue = realValue;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TopupRequestDTO topupRequestDTO = (TopupRequestDTO) o;
        if (topupRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), topupRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TopupRequestDTO{" +
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
