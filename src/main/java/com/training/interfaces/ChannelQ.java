package com.training.interfaces;

import com.training.constant.Channel;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({TYPE, FIELD, PARAMETER, METHOD})
public @interface ChannelQ {
    Channel value();
}
