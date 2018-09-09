package vn.com.cardmanagement.web.rest.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageUrlRequest implements Serializable {

    @JsonProperty("url")
    private String url;

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("uploader_id")
    private String uploaderId;

    @JsonProperty("uploader_name")
    private String uploaderName;

    @JsonProperty("image_type")
    private String imageType;

    @JsonProperty("domain_number")
    private String domainNumber;

    @JsonProperty("skip_google_check")
    private Boolean skipGoogleCheck;

    @JsonProperty("gender")
    private String gender;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getDomainNumber() {
        return domainNumber;
    }

    public void setDomainNumber(String domainNumber) {
        this.domainNumber = domainNumber;
    }

    public Boolean getSkipGoogleCheck() {
        return skipGoogleCheck;
    }

    public void setSkipGoogleCheck(Boolean skipGoogleCheck) {
        this.skipGoogleCheck = skipGoogleCheck;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isChatImage(){
        return "chat".equalsIgnoreCase(imageType);
    }

    public boolean isMailImage(){
        return "mail".equalsIgnoreCase(imageType);
    }


}

