package com.mmorpg.mir.model.formula;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.persistence.Transient;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 公式对象
 * 
 * @author frank
 */
@Resource
public class Formula {

	private static final ParserContext context = new ParserContext();

	static {
		/** 导入 {@link Math} 中的全部静态方法 */
		context.addImport(Math.class);
		for (Method method : Math.class.getMethods()) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				String name = method.getName();
				context.addImport(name, method);
			}
		}
	}

	/** 唯一标识 */
	@Id
	private String id;
	/** 公式内容 */
	private String content;
	/** 结果类型 */
	private Class<?> returnClz;
	/** 上下文类型 */
	private Class<?> ctxClz;

	/** 编译后的公式 */
	@Transient
	private transient volatile Serializable exp;

	/**
	 * 根据上下文计算表达式值
	 * 
	 * @param ctx
	 *            上下文对象
	 * @return
	 */
	public Object calculate(Object ctx) {
		if (exp == null) {
			synchronized (this) {
				if (exp == null) {
					exp = MVEL.compileExpression(content, context);
				}
			}
		}
		Object result = null;
		try {
			result = MVEL.executeExpression(exp, ctx, returnClz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[" + content + "] 解析错误");
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Class<?> getReturnClz() {
		return returnClz;
	}

	public Class<?> getCtxClz() {
		return ctxClz;
	}

	public void compile() {
		if (exp == null) {
			synchronized (this) {
				exp = MVEL.compileExpression(content, context);
			}
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReturnClz(Class<?> returnClz) {
		this.returnClz = returnClz;
	}

	public void setCtxClz(Class<?> ctxClz) {
		this.ctxClz = ctxClz;
	}

}
