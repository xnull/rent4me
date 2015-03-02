package bynull.realty.hibernate.validation.validators;

import bynull.realty.hibernate.validation.annotations.LessThanOrEqual;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author dionis on 25/11/14.
 */
public class LessThanOrEqualValidator implements ConstraintValidator<LessThanOrEqual, Object> {

    private String targetField;
    private String fieldForComparison;

    @Override
    public void initialize(LessThanOrEqual annotation) {
        targetField = annotation.targetField();
        fieldForComparison = annotation.fieldForComparison();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return false;
        Class<?> aClass = value.getClass();
        try {
            //TODO: ehnance
            Field target;
            try {
                target = aClass.getDeclaredField(targetField);
            } catch (NoSuchFieldException e) {
                Class<?> superclass = aClass.getSuperclass();
                target = superclass.getDeclaredField(targetField);
            } catch (SecurityException e) {
                throw e;
            }
            Field forComparison;
            try {
                forComparison = aClass.getDeclaredField(fieldForComparison);
            } catch (NoSuchFieldException e) {
                Class<?> superclass = aClass.getSuperclass();
                forComparison = superclass.getDeclaredField(fieldForComparison);
            } catch (SecurityException e) {
                throw e;
            }
            Number targetValue;
            Number forComparisonValue;
            {
                boolean accessible = target.isAccessible();
                target.setAccessible(true);
                try {
                    targetValue = (Number) target.get(value);
                } finally {
                    target.setAccessible(accessible);
                }
            }

            {
                boolean accessible = target.isAccessible();
                forComparison.setAccessible(true);
                try {
                    forComparisonValue = (Number) forComparison.get(value);
                } finally {
                    forComparison.setAccessible(accessible);
                }
            }

            boolean result = targetValue != null && forComparisonValue != null && targetValue.longValue() <= forComparisonValue.longValue();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
