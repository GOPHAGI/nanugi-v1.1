package com.gophagi.nanugi.groupbuying.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	ONGOING("진행중"), DONE("완료");

	String name;

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
			case "진행중":
				return Status.ONGOING;
			case "완료":
				return Status.DONE;
		}
		return Status.ONGOING;
	}
}
