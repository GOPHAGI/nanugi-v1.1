package com.gophagi.nanugi.groupbuying.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	GATHERING("모집중"), ONGOING("모집완료"), DONE("진행완료");

	final String name;

	Status(String name) {
		this.name = name;
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static Status fromValue(String value) {
		switch (value) {
			case "모집중":
				return Status.GATHERING;
			case "모집완료":
				return Status.ONGOING;
			case "진행완료":
				return Status.DONE;
		}
		return Status.ONGOING;
	}
}
