/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;

/**
 *
 * use hibernate to format queries
 *
 * @author rajakolli
 * @version 1: 0
 */
public class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
    
    private Formatter formatter = FormatStyle.BASIC.getFormatter();

    /** {@inheritDoc} */
    @Override
    protected String formatQuery(String query) {
        return this.formatter.format(query);
    }

}
