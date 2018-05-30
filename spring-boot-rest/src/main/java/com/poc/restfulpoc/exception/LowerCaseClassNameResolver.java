/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.exception;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * LowerCaseClassNameResolver.
 *
 * @author Raja Kolli
 *
 */
class LowerCaseClassNameResolver extends TypeIdResolverBase {

	/** {@inheritDoc} */

	@Override
	public String idFromValue(Object value) {
		return value.getClass().getSimpleName().toLowerCase(Locale.getDefault());
	}

	/** {@inheritDoc} */

	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		return idFromValue(value);
	}

	/** {@inheritDoc} */

	@Override
	public JsonTypeInfo.Id getMechanism() {
		return JsonTypeInfo.Id.CUSTOM;
	}

}
