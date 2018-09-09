package vn.com.cardmanagement.web.rest.params;


public class VideoSplitFrameQueryCondition {

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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

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

    private String videoId;

}
