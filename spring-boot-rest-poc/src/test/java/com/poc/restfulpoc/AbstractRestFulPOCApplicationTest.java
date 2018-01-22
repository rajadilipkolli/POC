/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.poc.restfulpoc.data.DataBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { RestFulPOCApplication.class,
      DataBuilder.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractRestFulPOCApplicationTest {

}
