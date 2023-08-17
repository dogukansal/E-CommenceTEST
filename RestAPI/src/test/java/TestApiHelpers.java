import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestApiHelpers {
	
	static String baseurl = "https://dummyjson.com";
	public static int getProductStock(int productId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/products/" + productId)
	              .then()
	              .extract()
	              .path("stock");
	  }

	public static Response addProductToBasket(JSONObject product) {
	      return RestAssured.given()
	              .header("Content-Type", "application/json")
	              .contentType(ContentType.JSON)
	              .body(product)
	              .when()
	              .post(baseurl+"/carts/add");
	  }
	  
	public static double getProductPrice(int productId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/products/" + productId)
	              .then()
	              .extract()
	              .path("price");
	  }
	  
	public static double getBasketTotalPrice(int userId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/carts/user/" + userId)
	              .then()
	              .extract()
	              .path("total");
	  }
	public static double getProductPriceInBasket(int userId , int productId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/carts/user/" + userId)
	              .then()
	              .extract()
	              .path("carts[0].products["+productId+"].price");
	  }
	  
	public static int getBasketProductCount(int userId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/carts/user/" + userId)
	              .then()
	              .extract()
	              .path("totalProduct");
	  }
	  public static double getProductTax(int productId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/product/" + productId)
	              .then()
	              .extract()
	              .path("tax");
	  }
	  public static double getProductTaxFree(int productId) {
	      return RestAssured
	              .when()
	              .get(baseurl+"/product/" + productId)
	              .then()
	              .extract()
	              .path("taxFree");
	  }
}
