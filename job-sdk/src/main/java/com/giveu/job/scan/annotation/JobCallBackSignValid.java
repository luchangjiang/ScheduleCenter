package com.giveu.job.scan.annotation;

import java.lang.annotation.*;

/**
 * Created by fox on 2018/11/26.
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface JobCallBackSignValid {
}
