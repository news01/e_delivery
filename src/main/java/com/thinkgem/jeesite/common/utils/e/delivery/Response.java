package com.thinkgem.jeesite.common.utils.e.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by viper on 2017/4/28.
 */
public class Response {

        @JsonProperty("success")
        private String success;

        @JsonProperty("failmailnums")
        private String failMailNums;

        @JsonProperty("remark")
        private String remark;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getFailMailNums() {
            return failMailNums;
        }

        public void setFailMailNums(String failMailNums) {
            this.failMailNums = failMailNums;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

}
