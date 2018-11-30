package com.study.webapp.enums;
/**
 * 抽象的枚举类
 * 
 * @author xwb
 * 
 * @param <T>
 */
public interface AbstractEnumInterface<T> {

	/**
	 * 获得当前枚举类的int值
	 * 
	 * @return
	 */
	public int getIntValue();

	/**
	 * 根据intValue生成对应的Enum对象
	 * 
	 * @param intValue
	 * @return
	 */
	public T genEnumByIntValue(int intValue);
}