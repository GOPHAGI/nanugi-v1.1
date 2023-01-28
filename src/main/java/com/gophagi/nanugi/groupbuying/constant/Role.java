package com.gophagi.nanugi.groupbuying.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.groupbuying.exception.InvalidRoleException;

public enum Role {
	PROMOTER("개설자"), PARTICIPANT("참여자");

	String name;

	Role(String name) {
		this.name = name;
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static Role fromValue(String value) {
		switch (value) {
			case "PROMOTER":
				return Role.PROMOTER;
			case "PARTICIPANT":
				return Role.PARTICIPANT;
		}
		throw new InvalidRoleException(ErrorCode.INVALID_ROLE);
	}
}
