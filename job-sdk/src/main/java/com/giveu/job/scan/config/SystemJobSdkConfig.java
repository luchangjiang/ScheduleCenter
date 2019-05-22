package com.giveu.job.scan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by fox on 2018/8/20.
 */
@Configuration
@ImportResource(locations={"classpath:application-job-sdk.xml"})
public class SystemJobSdkConfig {

}
