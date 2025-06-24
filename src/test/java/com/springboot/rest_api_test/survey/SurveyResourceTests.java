package com.springboot.rest_api_test.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.springboot.survey.Question;
import com.springboot.survey.SurveyResource;
import com.springboot.survey.SurveyService;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
class SurveyResourceTests {
	@MockBean
	private SurveyService surveyService;

	@Autowired
	private MockMvc mockMvc;

	private static String SPECIFIC_SURVEY_QUESTIONS_URL = "/surveys/survey1/questions";
	private static String SPECIFIC_QUESTION_URL = "/surveys/survey1/questions/question1";

	@Test
	void retrieveAllSurveyQuestion_basicScenario() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_SURVEY_QUESTIONS_URL)
				.accept(MediaType.APPLICATION_JSON);

		Question question1 = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		Question question2 = new Question("Question2", "Fastest Growing Cloud Platform",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
		Question question3 = new Question("Question3", "Most Popular DevOps Tool",
				Arrays.asList("Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

		List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));

		when(surveyService.retrieveAllQuestionsFromSurvey("survey1")).thenReturn(questions);

		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

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
		int EXPECTED_STATUS_CODE = 200;

		String ACTUAL_REQUEST_BODY = mvcResult.getResponse().getContentAsString();
		int ACTUAL_STATUS_CODE = mvcResult.getResponse().getStatus();

		assertEquals(EXPECTED_STATUS_CODE, ACTUAL_STATUS_CODE);
		JSONAssert.assertEquals(EXPECTED_REQUEST_BODY, ACTUAL_REQUEST_BODY, false);
	}

	@Test
	void retrieveSurveyQuestion_basicScenario() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL)
				.accept(MediaType.APPLICATION_JSON);

		Optional<Question> question = Optional.ofNullable(new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS"));

		when(surveyService.retrieveQuestionFromSurvey("survey1", "question1")).thenReturn(question);

		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		String EXPECTED_REQUEST_BODY = """
				{
				  "id": "Question1",
				  "description": "Most Popular Cloud Platform Today",
				  "correctOption": "AWS"
				}
				""";
		int EXPECTED_STATUS_CODE = 200;

		String ACTUAL_REQUEST_BODY = mvcResult.getResponse().getContentAsString();
		int ACTUAL_STATUS_CODE = mvcResult.getResponse().getStatus();

		assertEquals(EXPECTED_STATUS_CODE, ACTUAL_STATUS_CODE);
		JSONAssert.assertEquals(EXPECTED_REQUEST_BODY, ACTUAL_REQUEST_BODY, false);
	}

	@Test
	void retrieveSurveyQuestion_404Scenario() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcReturn = mockMvc.perform(requestBuilder).andReturn();

		int EXPECTED_STATUS_CODE = 404;
		int ACTUAL_STATUS_CODE = mvcReturn.getResponse().getStatus();

		assertEquals(EXPECTED_STATUS_CODE, ACTUAL_STATUS_CODE);
	}

	@Test
	void addSurveyQuestion_basicScenario() throws Exception {
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

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SPECIFIC_SURVEY_QUESTIONS_URL)
				.accept(MediaType.APPLICATION_JSON).content(NEW_QUESTION_REQUEST_BODY).contentType(MediaType.APPLICATION_JSON);
		
		when(surveyService.addSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");
		
		MvcResult mvcReturn = mockMvc.perform(requestBuilder).andReturn();

		int EXPECTED_STATUS_CODE = 201;
		String EXPECTED_LOCATION = "http://localhost/surveys/survey1/questions/SOME_ID";
		
		int ACTUAL_STATUS_CODE = mvcReturn.getResponse().getStatus();
		String ACTUAL_LOCATION = mvcReturn.getResponse().getHeader("Location");


		System.out.println(ACTUAL_STATUS_CODE);
		System.out.println(ACTUAL_LOCATION);
		
		assertEquals(EXPECTED_STATUS_CODE, ACTUAL_STATUS_CODE);
		assertEquals(EXPECTED_LOCATION, ACTUAL_LOCATION);
	}
	
	@Test
	void addSurveyQuestion_400BadRequest() throws Exception {
		String NEW_QUESTION_REQUEST_BODY = """
				  {
				    "description": "Test Question",
				    "options": [
				      "Test",
				      "Dummy",
				    "correctOption": "Test"
				  }
				""";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SPECIFIC_SURVEY_QUESTIONS_URL)
				.accept(MediaType.APPLICATION_JSON).content(NEW_QUESTION_REQUEST_BODY).contentType(MediaType.APPLICATION_JSON);
		
		when(surveyService.addSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");
		
		MvcResult mvcReturn = mockMvc.perform(requestBuilder).andReturn();

		int EXPECTED_STATUS_CODE = 400;
		int ACTUAL_STATUS_CODE = mvcReturn.getResponse().getStatus();
		
		assertEquals(EXPECTED_STATUS_CODE, ACTUAL_STATUS_CODE);
	}

}
