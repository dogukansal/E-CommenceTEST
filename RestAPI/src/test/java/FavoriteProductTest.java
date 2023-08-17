import static org.hamcrest.Matchers.equalTo;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class FavoriteProductTest {
	static String baseurl = "https://dummyjson.com";
	@Test
	  public static void GetFavoritesOfUser_ReturnsStatusCode200() {   
		  
		  RestAssured
		   .given()
		   .when()
		      	.get(baseurl + "/users/5/favorites")
		   .then()
		      	.statusCode(200);
	   }
	  
	  @Test
	  public static void AddFavoritesProductOfUser__ReturnsStatusCode201() {   
		    JSONObject product = new JSONObject();

		    product.put("id", 77);
		    
		    RestAssured
		   .given()
		    	.header("Content-Type", "application/json")
		    	.contentType(ContentType.JSON)
		    	.body(product.toJSONString())
		   .when()
		      	.post(baseurl + "/users/5/favorites/add")
		   .then()
		      	.statusCode(201);
		    
	   }
	  public static void GetFavoritesNotification__ReturnsStatusCode200() {   
		    
		    RestAssured
		    .given()  
		    .when()
	        	.get(baseurl + "/users/5/favorites")
	        .then()
	        	.statusCode(200)
	        	.body("stock.message", equalTo("Product back in stock!")); //or 
		    //  .body("discount.message", equalTo("New discount available!"))  
	   }
	  
	  @Test
	  public static void DeleteFavoritesProductOfUser__ReturnsStatusCode204() {   
		    RestAssured
		   .given()
		    	.param("id", 77)
		   .when()
		      	.delete(baseurl + "/users/5/favorites/delete")
		   .then()
		      	.statusCode(204);
		    
	   }
}
