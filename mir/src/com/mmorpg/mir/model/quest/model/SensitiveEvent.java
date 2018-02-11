package com.mmorpg.mir.model.quest.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法敏感事件接口
 * 
 * @author Kuang Hao
 * @since v1.0 2013-12-19
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveEvent {

	/**
	 * 指令值，默认:0
	 * 
	 * @return
	 */
	int value() default 0;

	/**
	 * 指令所属模块声明，可选
	 * 
	 * @return
	 */
	Class<?>[] event() default {};
}
