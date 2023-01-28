package com.gophagi.nanugi.groupbuying.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.groupbuying.exception.InvalidCategoryException;

public enum Category {
	HOUSEHOLD_GOODS("생활용품");

	String name;

	Category(String name) {
		this.name = name;
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static Category fromValue(String value) {
		switch (value) {
			case "생활용품":
				return Category.HOUSEHOLD_GOODS;
		}
		throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
	}
}
