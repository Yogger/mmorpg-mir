package com.mmorpg.mir.model.common;

/**
 * 重新加载配置表
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-22
 * 
 */
public interface ResourceReload {
	/**
	 * 重新加载配置表完成后调用
	 */
	public void reload();

	public Class<?> getResourceClass();
}
