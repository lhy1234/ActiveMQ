package com.nb.delay;

import java.io.Serializable;
import java.util.Date;

public class NotifyPlan implements Serializable {

    private Integer id;

    private String notifyPlanId;

    private Integer businessType;

    private String objectId;

    private Integer templateId;

    private Date planTime;

    private String remark;

    private Integer executeStatus;

    private String batchId;

    private String createAccountId;

    private String createAccountName;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotifyPlanId() {
        return notifyPlanId;
    }

    public void setNotifyPlanId(String notifyPlanId) {
        this.notifyPlanId = notifyPlanId == null ? null : notifyPlanId.trim();
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId == null ? null : objectId.trim();
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getCreateAccountId() {
        return createAccountId;
    }

    public void setCreateAccountId(String createAccountId) {
        this.createAccountId = createAccountId == null ? null : createAccountId.trim();
    }

    public String getCreateAccountName() {
        return createAccountName;
    }

    public void setCreateAccountName(String createAccountName) {
        this.createAccountName = createAccountName == null ? null : createAccountName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}