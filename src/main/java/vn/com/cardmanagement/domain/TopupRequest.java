package vn.com.cardmanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Card.
 */
@Document(collection = "topup_request")
public class TopupRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final long TIMEOUT = 15 * 60 * 1000;// image link timeout is one hour
    private static final long CHANGE_LINK_TIME = 15 * 60 * 1000;// image link changed after 5 minutes

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

    public void setMobileService(String mobileService) {
        this.mobileService = mobileService;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopupRequest card = (TopupRequest) o;
        if (card.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), card.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void encodeId() {
        this.id = encodeId(this.id);
    }

    public static String encodeId(String id) {
        return id + "-" + Long.toHexString(System.currentTimeMillis() / CHANGE_LINK_TIME);
    }

    public static String decodeId(String id) {
        return decodeId(id, TIMEOUT);
    }

    public static String decodeId(String id, long timeout) {

        String[] parts = id.split("-");
        if (parts.length == 2) {
            long milli = Long.parseLong(parts[1], 16) * CHANGE_LINK_TIME;
            if (timeout == 0 || milli <= System.currentTimeMillis() && milli > System.currentTimeMillis() - timeout) {
                return parts[0];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", mobileService='" + getMobileService() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", userId='" + getUserId() + "'" +
            ", status='" + getStatus() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
