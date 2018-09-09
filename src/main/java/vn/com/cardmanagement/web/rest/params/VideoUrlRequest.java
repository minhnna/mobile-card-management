package vn.com.cardmanagement.web.rest.params;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class VideoUrlRequest implements Serializable {

    @JsonProperty("url")
    private String url;

    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("uploader_name")
    private String uploaderName;

    @JsonProperty("call_id")
    private String callId;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("video_type")
    private String videoType;

    @JsonProperty("domain_number")
    private String domainNumber;

    @JsonProperty("uploader_id")
    private String uploaderId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getDomainNumber() {
        return domainNumber;
    }

    public void setDomainNumber(String domainNumber) {
        this.domainNumber = domainNumber;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }
}

