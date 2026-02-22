/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Locale;
import tools.jackson.databind.jsontype.impl.TypeIdResolverBase;

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
