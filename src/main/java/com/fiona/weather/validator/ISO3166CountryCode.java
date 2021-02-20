package com.fiona.weather.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface ISO3166CountryCode {

	String message() default "Country should follow ISO 3166-1 alpha-2 standard";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
