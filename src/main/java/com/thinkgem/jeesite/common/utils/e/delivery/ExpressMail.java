package com.thinkgem.jeesite.common.utils.e.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by hp on 2017/3/27.
 */
public class ExpressMail {


    @JsonProperty("listexpressmail")
    private List<ExpressMailItem> listExpressMail;

    public List<ExpressMailItem> getListExpressMail() {
        return listExpressMail;
    }

    public void setListExpressMail(List<ExpressMailItem> listExpressMail) {
        this.listExpressMail = listExpressMail;
    }

    @Override
    public String toString() {
        return "ExpressMail{" +
                "listExpressMail=" + listExpressMail +
                '}';
    }

    public static class ExpressMailItem{
        @JsonProperty("serialnumber")
        private String serialNumber;

        @JsonProperty("mailnum")
        private String mailNum;

        @JsonProperty("procdate")
        private String procDate;

        @JsonProperty("proctime")
        private String procTime;

        @JsonProperty("orgfullname")
        private String orgFullName;

        @JsonProperty("action")
        private String action;

        @JsonProperty("description")
        private String description;

        @JsonProperty("effect")
        private String effect;

        @JsonProperty("properdelivery")
        private String properDelivery;

        @JsonProperty("notproperdelivery")
        private String notProperDelivery;

        @JsonIgnore
        private Timestamp timestamp;


        public Timestamp getTimestamp() {
            return timestamp;
        }


        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getMailNum() {
            return mailNum;
        }

        public void setMailNum(String mailNum) {
            this.mailNum = mailNum;
        }

        public String getProcDate() {
            return procDate;
        }

        public void setProcDate(String procDate) {
            this.procDate = procDate;
        }

        public String getProcTime() {
            return procTime;
        }

        public void setProcTime(String procTime) {
            this.procTime = procTime;
        }

        public String getOrgFullName() {
            return orgFullName;
        }

        public void setOrgFullName(String orgFullName) {
            this.orgFullName = orgFullName;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        public String getProperDelivery() {
            return properDelivery;
        }

        public void setProperDelivery(String properDelivery) {
            this.properDelivery = properDelivery;
        }

        public String getNotProperDelivery() {
            return notProperDelivery;
        }

        public void setNotProperDelivery(String notProperDelivery) {
            this.notProperDelivery = notProperDelivery;
        }

        @Override
        public String toString() {
            return "ExpressMailItem{" +
                    "serialNumber='" + serialNumber + '\'' +
                    ", mailNum='" + mailNum + '\'' +
                    ", procDate='" + procDate + '\'' +
                    ", procTime='" + procTime + '\'' +
                    ", orgFullName='" + orgFullName + '\'' +
                    ", action=" + action +
                    ", description='" + description + '\'' +
                    ", effect='" + effect + '\'' +
                    ", properDelivery='" + properDelivery + '\'' +
                    ", notProperDelivery='" + notProperDelivery + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }


}
