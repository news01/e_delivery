package com.thinkgem.jeesite.modules.userwechat.entity;


public class TemplateMsgResult extends ResultState{
	private static final long serialVersionUID = 3198012785950215862L;  
	  
    private String msgid; // 消息id(发送模板消息)  
  
    public String getMsgid() {  
        return msgid;  
    }  
  
    public void setMsgid(String msgid) {  
        this.msgid = msgid;  
    }  
}
