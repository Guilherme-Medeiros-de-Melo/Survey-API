package com.springboot.survey;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class SurveyService {
	private static List<Survey> surveys = new ArrayList<>();

	static {
		Question question1 = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		Question question2 = new Question("Question2", "Fastest Growing Cloud Platform",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
		Question question3 = new Question("Question3", "Most Popular DevOps Tool",
				Arrays.asList("Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

		List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));

		Survey survey = new Survey("Survey1", "My Favorite Survey", "Description of the Survey", questions);

		surveys.add(survey);
	}

	public List<Survey> retrieveAllSurveys() {
		return surveys;
	}

	public Survey retrieveSurveyById(String surveyId) {
		Predicate<? super Survey> predicate = survey -> survey.getId().equalsIgnoreCase(surveyId);
		Optional<Survey> optionalSurvey = surveys.stream().filter(predicate).findFirst();

		if (optionalSurvey.isEmpty()) {
			return null;
		}

		return optionalSurvey.get();
	}

	public List<Question> retrieveAllQuestionsFromSurvey(String surveyId) {
		Survey survey = retrieveSurveyById(surveyId);

		if (survey == null) {
			return null;
		}

		return survey.getQuestions();
	}

	public Optional<Question> retrieveQuestionFromSurvey(String surveyId, String questionId) {
		List<Question> surveyQuestions = retrieveAllQuestionsFromSurvey(surveyId);

		if (surveyQuestions == null) {
			return null;
		}

		Predicate<? super Question> predicate = q -> q.getId().equalsIgnoreCase(questionId);
		Optional<Question> optionalQuestion = surveyQuestions.stream().filter(predicate)
				.findFirst();

		if (optionalQuestion == null) {
			return null;
		}

		return optionalQuestion;
	}

	public String addSurveyQuestion(String surveyId, Question question) {
		List<Question> questions = retrieveAllQuestionsFromSurvey(surveyId);
		
		SecureRandom secureRandom = new SecureRandom();
		String randomId = new BigInteger(32, secureRandom).toString();
		
		question.setId(randomId);
		questions.add(question);
		
		return question.getId();
	}

	public String deleteSurveyQuestion(String surveyId, String questionId) {
		List<Question> surveyQuestions = retrieveAllQuestionsFromSurvey(surveyId);

		if (surveyQuestions == null) {
			return null;
		}

		Predicate<? super Question> predicate = q -> q.getId().equalsIgnoreCase(questionId);
		
		boolean removedQuestion = surveyQuestions.removeIf(predicate);
		
		if(!removedQuestion) {
			return null;
		}
		
		return questionId;
	}

	public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
		List<Question> surveyQuestions = retrieveAllQuestionsFromSurvey(surveyId);

		Predicate<? super Question> predicate = q -> q.getId().equalsIgnoreCase(questionId);
		surveyQuestions.removeIf(predicate);

		surveyQuestions.add(question);
	}
}
