package com.cfp.system.thread;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class StartupThread implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		//初始化代码
		
	}
}
