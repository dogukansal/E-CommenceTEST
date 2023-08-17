import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class FindProductTest {
	
	  static String baseurl = "https://dummyjson.com";
	  @Test
	  public static void findProducts_SearchProductWithId_ReturnsStatusCode200AndProduct() {
	    RestAssured.
	    given()
	      .when()
	      	.get(baseurl + "/products/1")
	      .then()
	      	.statusCode(200).log().all();
	  }
	  @Test
	  public static void findProducts_AllProduct_ReturnsStatusCode200AndProduct() {
	    RestAssured.
	    given()
	      .when()
	      	.get(baseurl + "/products")
	      .then()
	      	.statusCode(200).log().all();
	  }
	  @Test
	  public static void findProducts_SearchProductWitdIdOfNonExist_ReturnsStatusCode404NotFound() {
	    RestAssured.
	    given()
	      .when()
	      	.get(baseurl + "/products/900000")
	      .then()
	      	.statusCode(404).log().all();
	  }

	  @Test
	  public static void findProducts_SearchProductsOfNonExist_ReturnsStatusCode200AndEmpty() {
	    RestAssured.
	    given()
	      .when()
	      	.get(baseurl + "/products/search?q=abc")
	      .then()
	      	.statusCode(200) 
	      	.log().all();
	  }
	  
	  @Test
	  public static void findProducts_SearchProductsWithSearchParam_ReturnsStatusCode200AndProducts() {
	    RestAssured.
	    given()
	      .when()
	      	.get(baseurl + "/products/search?q=phone")
	      .then()
	      	.statusCode(200)
	      	.log().all();
	  }
}
