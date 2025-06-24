package com.springboot.rest_api_test.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class JsonAssertTests {

	@Test
	void jsonAssert_basics() throws JSONException {
		String expectedResponse = """
		{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctOption":"AWS"}
		""";
		
		String actualResponse = """
				  {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctOption":"AWS"}
				""";
		
		JSONAssert.assertEquals(expectedResponse, actualResponse, true);
	}

}
