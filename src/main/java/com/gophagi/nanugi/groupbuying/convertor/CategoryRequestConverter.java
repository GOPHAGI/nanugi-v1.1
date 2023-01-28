package com.gophagi.nanugi.groupbuying.convertor;

import org.springframework.core.convert.converter.Converter;

import com.gophagi.nanugi.groupbuying.constant.Category;

public class CategoryRequestConverter implements Converter<String, Category> {
	@Override
	public Category convert(String name) {
		return Category.fromValue(name);
	}
}
