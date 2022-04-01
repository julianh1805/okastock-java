package com.julianhusson.okastock.configuration;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String principal() default "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
    String credentials() default "password";
    String[] authorities() default {"USER"};
}
