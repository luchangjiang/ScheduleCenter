package com.giveu.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/22.
 */
@Component
public class CommLock {
	@Value("${job.lock.open}")
	public volatile boolean isOpen;
}
