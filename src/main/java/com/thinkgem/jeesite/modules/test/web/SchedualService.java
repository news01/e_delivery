package com.thinkgem.jeesite.modules.test.web;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SchedualService {
	Logger logger = Logger.getLogger(SchedualService.class);
//	@Scheduled(cron="0 0 8 * * ? *")
	@Scheduled(fixedRate = 100 * 1)
	public void scheduled(){
		System.out.println("==========================");
	}
}
