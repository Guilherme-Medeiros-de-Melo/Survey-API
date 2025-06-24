package com.springboot.rest_api_test.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class SurveyResourceIntegrationTests {

	@Autowired
	private TestRestTemplate template;

	private static String GENERIC_QUESTION_URL = "/surveys/survey1/questions";

	@Test
	void retrieveSurveyQuestion_basicScenario() throws JSONException {
		String SPECIFIC_QUESTION_URL = "/surveys/survey1/questions/question1";
		String EXPECTED_REQUEST_BODY = """
					{
					  "id": "Question1",
					  "description": "Most Popular Cloud Platform Today",
					  "correctOption": "AWS"
					}
				""";

		HttpHeaders headers = createHttpsHeadersTypeAuthorization();

		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity,
				String.class);

		// ResponseEntity<String> responseEntity =
		// template.getForEntity(SPECIFIC_QUESTION_URL, String.class);

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
		JSONAssert.assertEquals(EXPECTED_REQUEST_BODY, responseEntity.getBody(), false);
	}

	@Test
	void retrieveAllSurveyQuestions_basicScenario() throws JSONException {
		String EXPECTED_REQUEST_BODY = """
				[
				  {
				    "id": "Question1",
				    "correctOption": "AWS"
				  },
				  {
				    "id": "Question2",
				    "correctOption": "Google Cloud"
				  },
				  {
				    "id": "Question3",
				    "correctOption": "Kubernetes"
				  }
				]
				""";
		
		HttpHeaders headers = createHttpsHeadersTypeAuthorization();

		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.GET, httpEntity,
				String.class);

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
		JSONAssert.assertEquals(EXPECTED_REQUEST_BODY, responseEntity.getBody(), false);
	}

	@Test
	void addSurveyQuestion_basicScenario() throws JSONException {
		String NEW_QUESTION_REQUEST_BODY = """
				  {
				    "description": "Test Question",
				    "options": [
				      "Test",
				      "Dummy",
				      "Nada"
				    ],
				    "correctOption": "Test"
				  }
				""";

		HttpHeaders headers = createHttpsHeadersTypeAuthorization();

		HttpEntity<String> httpEntity = new HttpEntity<String>(NEW_QUESTION_REQUEST_BODY, headers);

		ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.POST, httpEntity,
				String.class);

		String LOCATION_HEADER = responseEntity.getHeaders().get("Location").get(0);
		HttpStatusCode STATUS_CODE = responseEntity.getStatusCode();

		assertTrue(STATUS_CODE.is2xxSuccessful());
		assertTrue(LOCATION_HEADER.contains(GENERIC_QUESTION_URL));

		ResponseEntity<String> responseEntityDelete = template.exchange(LOCATION_HEADER, HttpMethod.DELETE, httpEntity,
				String.class);
		
		assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());
		//template.delete(LOCATION_HEADER);
	}

	String performBasicAuthEncoding(String user, String password) {
		String combined = user + ":" + password;

		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());

		return new String(encodedBytes);
	}

	HttpHeaders createHttpsHeadersTypeAuthorization() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin", "password"));

		return headers;
	}
}
