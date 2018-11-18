package com.thinkgem.jeesite.test.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.thinkgem.jeesite.modules.archive.dao.ArchivesDao;
import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.service.ArchivesService;
import com.thinkgem.jeesite.modules.archivetrank.dao.ArchivesTrankDao;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.service.ArchivesTrankService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserWechatService;

import sun.print.resources.serviceui;
@Controller
public class TestMain {
	private SystemService service = new SystemService();
	@Autowired
	private ArchivesTrankDao trankDao;
	@Autowired
	private ArchivesDao archivesDao;
	@Autowired
	private ArchivesService archivesService;
	@RequestMapping(value="main")
	public void test() {
		List<Archives> list = archivesDao.findMoHu("?");
		List<Archives> list2 = new ArrayList<Archives>();
		String string = "";
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setCaseNum(rep(list.get(i).getCaseNum()));
			archivesService.save(list.get(i));
		}
	}
	public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
        int n = gbkStr.length();  
        byte[] utfBytes = new byte[3 * n];  
        int k = 0;  
        for (int i = 0; i < n; i++) {  
            int m = gbkStr.charAt(i);  
            if (m < 128 && m >= 0) {  
                utfBytes[k++] = (byte) m;  
                continue;  
            }  
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
        }  
        if (k < utfBytes.length) {  
            byte[] tmp = new byte[k];  
            System.arraycopy(utfBytes, 0, tmp, 0, k);  
            return tmp;  
        }  
        return utfBytes;  
    }  
	public List<Archives> archives(){
		return archivesDao.findMoHu("?");
	}
	public static void main(String[] args) {
		String string = "锛?2018锛夌菠0305鍒戝垵1鍙?锛?2018锛夌菠0305鍒戝垵2鍙?锛?2018锛夌菠0305鍒戝垵4鍙?锛?2018锛夌菠0305鍒戝垵5鍙?锛?2018锛夌菠0305鍒戝垵6鍙?锛?2018锛夌菠0305鍒戝垵11鍙?锛?2018锛夌菠0305鍒戝垵12鍙?锛?2018锛夌菠0305鍒戝垵13鍙?锛?2018锛夌菠0305鍒戝垵15鍙?锛?2018锛夌菠0305鍒戝垵16鍙?锛?2018锛夌菠0305鍒戝垵17鍙?锛?2018锛夌菠0305鍒戝垵18鍙?锛?2018锛夌菠0305鍒戝垵19鍙?锛?2018锛夌菠0305鍒戝垵3鍙?锛?2018锛夌菠0305鍒戝垵20鍙?锛?2018锛夌菠0305鍒戝垵23鍙?锛?2018锛夌菠0305鍒戝垵22鍙?锛?2018锛夌菠0305鍒戝垵35鍙?锛?2018锛夌菠0305鍒戝垵38鍙?锛?2018锛夌菠0305鍒戝垵40鍙?锛?2018锛夌菠0305鍒戝垵41鍙?锛?2018锛夌菠0305鍒戝垵43鍙?锛?2018锛夌菠0305鍒戝垵44鍙?锛?2018锛夌菠0305鍒戝垵46鍙?锛?2018锛夌菠0305鍒戝垵48鍙?锛?2018锛夌菠0305鍒戝垵50鍙?锛?2018锛夌菠0305姘戝垵106鍙?(2018)绮?0305鎵?89鍙?(2018)绮?0305鎵?90鍙?(2018)绮?0305鎵?91鍙?(2018)绮?0305鎵?92鍙?(2018)绮?0305鎵?93鍙?(2018)绮?0305鎵?94鍙?锛?2018锛夌菠0305姘戝垵193鍙?锛?2018锛夌菠0305姘戝垵217鍙?(2018)绮?0305鎵?110鍙?(2018)绮?0305鎵?111鍙?(2018)绮?0305鎵?123鍙?锛?2018锛夌菠0305姘戝垵287鍙?锛?2018锛夌菠0305姘戝垵292鍙?(2018)绮?0305鎵?133鍙?(2018)绮?0305鎵?134鍙?锛?2018锛夌菠0305姘戝垵324鍙?锛?2018锛夌菠0305姘戝垵337鍙?锛?2018锛夌菠0305姘戝垵347鍙?锛?2018锛夌菠0305姘戝垵352鍙?锛?2018锛夌菠0305姘戝垵374鍙?锛?2018锛夌菠0305姘戝垵376鍙?(2018)绮?0305鎵?173鍙?(2018)绮?0305鎵?175鍙?(2017)绮?0305鎵ф仮234-253鍙?(2018)绮?0305鎵?177鍙?(2018)绮?0305鎵?187鍙?(2018)绮?0305鎵?188鍙?(2018)绮?0305鎵?189鍙?锛?2018锛夌菠0305姘戝垵519鍙?锛?2018锛夌菠0305姘戝垵553鍙?锛?2018锛夌菠0305姘戝垵552鍙?锛?2018锛夌菠0305姘戝垵560鍙?锛?2018锛夌菠0305姘戝垵537鍙?锛?2018锛夌菠0305姘戝垵588鍙?锛?2018锛夌菠0305姘戝垵646鍙?(2017)绮?0305鎵ф仮282-301鍙?锛?2018锛夌菠0305姘戝垵840鍙?锛?2018锛夌菠0305姘戝垵1007鍙?(2018)绮?0305鎵?345-351鍙?(2018)绮?0305鎵?356-357鍙?(2018)绮?0305鎵?358-369鍙?锛?2018锛夌菠0305姘戝垵900鍙?(2018)绮?0305鎵?376-388鍙?锛?2018锛夌菠0305姘戝垵1496鍙?(2018)绮?0305鎵?409-410鍙?(2018)绮?0305鎵?421-432鍙?锛?2018锛夌菠0305姘戝垵1512鍙?锛?2018锛夌菠0305姘戝垵1527鍙?锛?2018锛夌菠0305姘戝垵1881鍙?锛?2018锛夌菠0305姘戝垵1896鍙?锛?2018锛夌菠0305姘戝垵1919鍙???2017????0305??204??锛?2018锛夌菠0305姘戝垵1983鍙?锛?2018锛夌菠0305姘戝垵1993鍙?(2018)绮?0305鎵?573鍙?(2018)绮?0305鎵?575-577鍙?锛?2018锛夌菠0305姘戝垵2011鍙?(2018)绮?0305鎵?580鍙?锛?2018锛夌菠0305姘戝垵2058鍙?(2018)绮?0305鎵?589-591鍙?(2018)绮?0305鎵?575-577銆?627鍙?(2018)绮?0305鎵?628-629鍙?锛?2018锛夌菠0305姘戝垵2143鍙?(2018)绮?0305鎵?651-659鍙?(2018)绮?0305鎵?661銆?663鍙?(2018)绮?0305鎵?818鍙?(2018)绮?0305鎵?819鍙?(2018)绮?0305鎵?820鍙?(2018)绮?0305鎵?821鍙?(2018)绮?0305鎵?822-823鍙?(2018)绮?0305鎵?824鍙?(2018)绮?0305鎵?825-826鍙?锛?2018锛夌菠0305姘戝垵2535鍙?锛?2018锛夌菠0305姘戝垵2687鍙?锛?2018锛夌菠0305姘戝垵2736鍙?锛?2018锛夌菠0305姘戝垵2745鍙?锛?2018锛夌菠0305姘戝垵2756鍙?(2018)绮?0305鎵?827-849鍙?锛?2018锛夌菠0305姘戝垵2766鍙?锛?2018锛夌菠0305姘戝垵2839鍙?(2018)绮?0305鎵?871-894鍙?锛?2018锛夌菠0305姘戝垵2892鍙?锛?2018锛夌菠0305姘戝垵2902鍙?(2018)绮?0305鎵?896-906鍙?锛?2018锛夌菠0305姘戝垵2909鍙?锛?2018锛夌菠0305姘戝垵2910鍙?锛?2018锛夌菠0305姘戝垵2913鍙?锛?2018锛夌菠0305姘戝垵2930鍙?(2018)绮?0305鎵?919-927鍙?(2018)绮?0305鎵?928-931鍙?(2018)绮?0305鎵?932-937鍙?锛?2018锛夌菠0305姘戝垵2955鍙?(2018)绮?0305鎵?938-942鍙?(2018)绮?0305鎵?944-946鍙?锛?2018锛夌菠0305姘戝垵2976鍙?锛?2018锛夌菠0305姘戝垵2989鍙?锛?2018锛夌菠0305姘戝垵3092鍙?锛?2018锛夌菠0305姘戝垵3096鍙?锛?2018锛夌菠0305姘戝垵3105鍙?锛?2018锛夌菠0305姘戝垵3108鍙?(2018)绮?0305鎵?957-958鍙?锛?2018锛夌菠0305姘戝垵3121鍙?(2018)绮?0305鎵?977-978鍙?(2018)绮?0305鎵?988-993鍙?(2018)绮?0305鎵?1000-1001鍙?";
		string = string.replace("锛?", "（");
		string = string.replace("锛夌菠", "）粤");
		string = string.replace("绮?", "粤");
		string = string.replace("鍙?", "号");
		string = string.replace("鍒戝垵", "刑初");
		string = string.replace("姘戝垵", "民初");
		string = string.replace("鎵?", "执");
		String a = "执恢";
		System.out.println(string);
		try {
			System.out.println(new String(a.getBytes("UTF-8"), "GBK"));
//			System.out.println(new String(b.getBytes("UTF-8"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String rep(String string){
		string = string.replace("锛?", "（");
		string = string.replace("锛夌菠", "）粤");
		string = string.replace("绮?", "粤");
		string = string.replace("鍙?", "号");
		string = string.replace("鍒戝垵", "刑初");
		string = string.replace("姘戝垵", "民初");
		string = string.replace("鎵?", "执");
		string = string.replace("鎵ф仮", "执恢");
		return string;
	}
}
