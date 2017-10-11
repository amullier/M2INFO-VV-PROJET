package fr.istic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation will be used to indicate if a Type or a method have to mutated
 * @author romain
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MutateTest {
	
	/**
	 * the default value is false, it means that the mutation will be occured
	 * @return 
	 */
    boolean ignor() default false;

}
