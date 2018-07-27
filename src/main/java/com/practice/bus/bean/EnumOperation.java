package com.practice.bus.bean;

/**
 * 操作类型的枚举类
 *
 */
public enum EnumOperation {
	// 第一行一定要写枚举类的实例
	ADD("add"), MODIFY("modify"), DELETE("delete");

	final String operation;

	EnumOperation(String operation) {
		this.operation = operation;
	}
}