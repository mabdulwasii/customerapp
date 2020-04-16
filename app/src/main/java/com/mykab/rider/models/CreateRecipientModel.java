package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykab.rider.json.RecipientResponseDetails;

public class CreateRecipientModel {
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("integration")
    @Expose
    private Integer integration;

    @SerializedName("metadata")
    @Expose
    private Integer metadata;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("recipient_code")
    @Expose
    private String recipientCode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMetadata() {
        return metadata;
    }

    public void setMetadata(Integer metadata) {
        this.metadata = metadata;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @SerializedName("details")
    @Expose
    private RecipientResponseDetails details;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public RecipientResponseDetails getDetails() {
        return details;
    }

    public void setDetails(RecipientResponseDetails details) {
        this.details = details;
    }

}
