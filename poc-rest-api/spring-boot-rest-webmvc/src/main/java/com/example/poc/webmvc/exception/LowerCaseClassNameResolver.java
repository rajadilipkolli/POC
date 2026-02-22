/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Locale;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindContext;
import tools.jackson.databind.jsontype.impl.TypeIdResolverBase;

class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(DatabindContext ctxt, Object value) throws JacksonException {
        return value.getClass().getSimpleName().toLowerCase(Locale.getDefault());
    }

    @Override
    public String idFromValueAndType(DatabindContext ctxt, Object value, Class<?> suggestedType)
            throws JacksonException {
        return idFromValue(ctxt, value);
    }

    /** {@inheritDoc} */
    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
