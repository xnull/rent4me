package bynull.realty.hibernate.validation.annotations;

import bynull.realty.hibernate.validation.validators.LessThanOrEqualValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author dionis on 25/11/14.
 */
@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = LessThanOrEqualValidator.class)
@Documented
public @interface LessThanOrEqual {
    String message() default "{bynull.realty.hibernate.validation.constraints.LessThanOrEqual}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

//    int value();

    String targetField();
    String fieldForComparison();
}
