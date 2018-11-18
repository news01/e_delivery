package com.thinkgem.jeesite.modules.caseloginfo.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.modules.caseloginfo.entity
 * @Desc: ${end}
 * @date 2018/8/24  16:35
 */
@JsonIgnoreProperties(ignoreUnknown = true)//忽略未知属性
public class CaseLogDTO implements Serializable {


//    "serialnumber":"00000000000000000001",
//            "mailnum": "LK434266003CN",
//            "procdate": "20130702",
//            "proctime": "000100",
//            "orgfullname": "所在地名称",
//            "action": 00,
//            "description": "描述信息",
//            "effect":"",
//            "properdelivery":"",
//            "notproperdelivery":""


    public List<CaseLogDtoObj> listexpressmail;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CaseLogDtoObj implements Serializable{

        public String serialnumber;
        public String mailnum;
        public String procdate;
        public String proctime;
        public String orgfullname;
        public String action;
        public String description;
        public String effect;
        public String properdelivery;
        public String notproperdelivery;

        public CaseLogDtoObj () { }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getMailnum() {
            return mailnum;
        }

        public void setMailnum(String mailnum) {
            this.mailnum = mailnum;
        }

        public String getProcdate() {
            return procdate;
        }

        public void setProcdate(String procdate) {
            this.procdate = procdate;
        }

        public String getProctime() {
            return proctime;
        }

        public void setProctime(String proctime) {
            this.proctime = proctime;
        }

        public String getOrgfullname() {
            return orgfullname;
        }

        public void setOrgfullname(String orgfullname) {
            this.orgfullname = orgfullname;
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

        public String getProperdelivery() {
            return properdelivery;
        }

        public void setProperdelivery(String properdelivery) {
            this.properdelivery = properdelivery;
        }

        public String getNotproperdelivery() {
            return notproperdelivery;
        }

        public void setNotproperdelivery(String notproperdelivery) {
            this.notproperdelivery = notproperdelivery;
        }


    }


    public List<CaseLogDtoObj> getListexpressmail() {
        return listexpressmail;
    }

    public void setListexpressmail(List<CaseLogDtoObj> listexpressmail) {
        this.listexpressmail = listexpressmail;
    }

    public static void main(String[] args) {
        CaseLogDTO t= new CaseLogDTO();
        List<CaseLogDtoObj> l = new ArrayList<>();
        CaseLogDTO.CaseLogDtoObj a = new CaseLogDTO().new CaseLogDtoObj();
        a.setAction("test");
        l.add(a);
        t.setListexpressmail(l);

        System.out.println(JSON.toJSONString(t));
    }
}
