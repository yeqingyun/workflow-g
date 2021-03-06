/**
 * @(#) CustomDateSerializer.java Created on 2014年11月17日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * The class <code>CustomDateSerializer</code>
 *
 * @author lipw
 * @version 1.0
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
	}

}
