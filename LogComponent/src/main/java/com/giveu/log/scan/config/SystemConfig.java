package com.giveu.log.scan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by fox on 2018/8/20.
 */
@Configuration
@ImportResource(locations={"classpath:application-log.xml"})
public class SystemConfig {

}
