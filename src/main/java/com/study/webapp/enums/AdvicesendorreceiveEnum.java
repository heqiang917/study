package com.study.webapp.enums;


/**
 * 状态 0:删除 1：默认状态
 * 
 * @author yguo
 * 
 */
public enum AdvicesendorreceiveEnum implements AbstractEnumInterface<AdvicesendorreceiveEnum> {

	/**
	 * 
	 */
	receive(1, "收到的咨询请求"),

	/**
	 * 
	 */
	send(2, "发送的咨询请求");
	/**
	 * 
	 */
	

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 构造函数
	 *
	 * @param v
	 * @param d
	 */
	private AdvicesendorreceiveEnum(int v, String d) {
		value = v;
		desc = d;
	}

	
	public AdvicesendorreceiveEnum genEnumByIntValue(int intValue) {
		for (AdvicesendorreceiveEnum item : AdvicesendorreceiveEnum.values()) {
			if (item.value == intValue)
				return item;
		}
		return receive;
	}

	public byte getByteValue(){
		return (byte)value;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
