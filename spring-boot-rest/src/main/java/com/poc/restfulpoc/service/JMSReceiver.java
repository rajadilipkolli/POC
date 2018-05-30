/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

/**
 * <p>
 * JMSReceiver interface.
 * </p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
public interface JMSReceiver {

	/**
	 * <p>
	 * receiveMessage.
	 * </p>
	 * @param customerId a {@link java.lang.String} object.
	 */
	void receiveMessage(String customerId);

}
