package com.apestech.framework.event;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface Listener {
    String value() default "";
}
