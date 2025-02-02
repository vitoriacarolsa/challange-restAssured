package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScoreControllerRA {
	private Long  nonExistingMovieId;

	private String adminUsername, adminPassword;
	private String adminToken;
	private Map<String, Object> putScoreInstance;

	@BeforeEach
	public void setup() throws JSONException {
		baseURI = "http://localhost:8080";

		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

		putScoreInstance = new HashMap<>();
		putScoreInstance.put("title", "Test Movie");
		putScoreInstance.put("score", 0.0F);
		putScoreInstance.put("count", 0.0);
		putScoreInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

	}

	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		JSONObject product = new JSONObject(putScoreInstance);
		nonExistingMovieId = 100L;

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(product)
				.when()
				.put("/scores/{id}", nonExistingMovieId)
				.then()
				.statusCode(404)
				.body("error", equalTo("Not Found"))
				.body("status", equalTo(404));

	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putScoreInstance.put("movieId", "");
		JSONObject product = new JSONObject(putScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(product)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		putScoreInstance.put("score", -2);
		JSONObject product = new JSONObject(putScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(product)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}


}
