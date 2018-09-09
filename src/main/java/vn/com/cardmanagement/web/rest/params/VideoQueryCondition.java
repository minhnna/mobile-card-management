package vn.com.cardmanagement.web.rest.params;


public class VideoQueryCondition {
    public enum CheckedBy {
        All,
        Human,
        AI,
    }
    public enum VideoStatus {
    	Pending,
        Approved,
        Rejected,
    }

    public CheckedBy getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(CheckedBy checkedBy) {
        this.checkedBy = checkedBy;
    }

    public VideoStatus getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(VideoStatus videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
	/**
     * Filter image by checker. Value: Human, AI, All
     */
    private CheckedBy checkedBy;
    /**
     * Filter image by status. Value: OK, NG, NC
     */
    private VideoStatus videoStatus;

    /**
     * Uploader Id
     */
    private String uploaderId;
    /**
     * tenantId
     */
    private String tenantId;
    /**
     * serviceId
     */
    private String serviceId;

}
