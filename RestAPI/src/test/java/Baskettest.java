import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Baskettest {

  static String baseurl = "https://dummyjson.com";
  
  
  public static void main(String[] args) {
    Response response = RestAssured.get(baseurl);
    System.out.println(response.getStatusCode()); //200 ok ?
    System.out.println(response.getStatusLine());
    System.out.println(response.getTime());
    System.out.println(response.getHeader("content-type"));
  }
  
  															//ALL THIS TEST IS AN EXAMPLE USAGE, DOESNT WORK!
  
  //************************ NORMAL ADD PRODUCT TO BASKET, GET BASKET OF USER , DELETE PRODUCT BASKET OF USER, CHECK BASKET OF USER *******************

  @Test //NP
  public static void AddProductToBasket_ReturnsStatusCode201AndProduct() {

    JSONObject mainObject = new JSONObject();

    mainObject.put("userId", "1");

    JSONArray productsArray = new JSONArray();

    JSONObject product = new JSONObject();

    product.put("id", 77);
    product.put("quantity", 1);

    productsArray.add(product);

    mainObject.put("products", productsArray);
    //"userId":1,
    //"products" : [	{
    //"id": 77,
    //"title": "Rose Ring",  }  ]

    RestAssured.
    given()
      	.header("Content-Type", "application/json")
      	.contentType(ContentType.JSON)
      	.body(mainObject.toJSONString())
      .when()
      	.post(baseurl + "/carts/add")
      .then()
      	.statusCode(201) //have response! 200
      	.log().all();

  }

  @Test // After added Rose Ring in a user's basket 
  public static void GetBasketOfAUser_ReturnsStatusCode200AndProduct() {
    RestAssured.
    given()
      .when()
      	.get(baseurl + "/carts/user/5")
      .then()
      	.statusCode(200)
      	.body("carts[0].products[1].title", equalTo("Rose Ring"))
      	.log().all();
  }

  @Test 
  public static void DeleteAProductInAUserBasket_ReturnsStatusCode204AndProds() {
    RestAssured.
    given()
      	.param("title", "Rose Ring")
      .when()
      	.delete(baseurl + "/carts/user/5")
      .then()
      	.statusCode(204) //have response! 200
      	.log().all();
  }

  @Test 
  public static void CheckAfterDeletingTheProduct_ReturnsStatusCode404() {
    RestAssured.
    given()
      .when()
      	.get(baseurl + "/carts/user/5")
      .then()
      	.statusCode(404) //have response! 200
      	.body("carts[0].products[1].title", equalTo("Rose Ring"))
      	.log().all();
  }
  //******************************************************	TOTAL PRODUCT AND TOTAL BASKET CONTROL ************************************************************
  
  
  @Test
  public static void CheckProductPricesAndTotalBasketPrice_IsEquals() {
	  //Are total product prices and basket price equal?
      
      double totalPrice =TestApiHelpers.getBasketTotalPrice(1);
      int totalProductInBasket = TestApiHelpers.getBasketProductCount(1);
      
     double checkTotalPrice=0;
     
      for (int i = 0; i < totalProductInBasket; i++) {
    	  checkTotalPrice += TestApiHelpers.getProductPriceInBasket(1, i);
		
	  }
	    assertEquals(totalPrice , checkTotalPrice);
	  }
  
  
  public static void CheckProductTotalCountInBasket_IsEquals() {
	  int totalProductInBasketBeforeAdd = TestApiHelpers.getBasketProductCount(1);
	  
	  JSONObject product = new JSONObject();
      product.put("id", 1234);
      product.put("title", "iPhone 9");
      
      Response addBasketResponse = TestApiHelpers.addProductToBasket(product);
      //assertEquals(addBasketResponse.getStatusCode(), 201);
      int totalProductInBasketAfterAdd =TestApiHelpers. getBasketProductCount(1);
      
	    assertEquals(totalProductInBasketBeforeAdd + 1, totalProductInBasketAfterAdd );
	  }

  
  //******************************************************	STOCK CONTROL ************************************************************

  
  @Test
  public void DecreasesStockWhenAddingProductToBasket_IsEquals() {
      //int initialStock = getProductStock(123);
      
      JSONObject product = new JSONObject();
      product.put("id", 1234);
      product.put("title", "iPhone 9");
      product.put("description", "An apple mobile which is nothing like apple");
      product.put("price", 12.96);
      product.put("discountPercentage", 549);
      product.put("stock", 94);
      product.put("brand", "Apple");
      product.put("category", "smartphones");
      
      Response addBasketResponse = TestApiHelpers.addProductToBasket(product);
      //assertEquals(addBasketResponse.getStatusCode(), 201);

      int stockAfterAdd = TestApiHelpers.getProductStock(1234);
      assertEquals(stockAfterAdd, 93);  //94 - 1
  }
  
  @Test
  public void AddingOutOfStockProductToBasket_ReturnsStatusCode400() {
	  //Suppose there is zero product left
	  
      JSONObject product = new JSONObject();
      product.put("id", 123);
      product.put("quantity", 1);
      
      Response firstAddResponse = TestApiHelpers.addProductToBasket(product);
      assertEquals(firstAddResponse.getStatusCode(), 400); 

  }

 
  
  //******************************************************	CARGO PRICE CONTROL ************************************************************

  
  @Test
  public static void FreeCargoPricingTestInBasket_IsEquals() {  
	  // one product

	   double ProductPrice = TestApiHelpers.getProductPrice(1);
	   
	   JSONObject product = new JSONObject();
	   product.put("id", 1);
	   product.put("quantity", 1);
		  
	    Response addBasketResponse = TestApiHelpers.addProductToBasket(product);
	    /*assertEquals(addBasketResponse.getStatusCode(), 201);  //check added product to basket */
	   
	    double totalPriceAfterAddBasket = TestApiHelpers.getBasketTotalPrice(1);
	   assertEquals(totalPriceAfterAddBasket, ProductPrice); 
  } 
  
  @Test
  public static void NoFreeCargoPricingTestInBasket_IsEquals() {  
	  //Suppose the price of the shipping fee is 10 and one product
	  int cargoPrice = 10;
	  	
	  double ProductPrice = TestApiHelpers.getProductPrice(1);
	   
	   JSONObject product = new JSONObject();
	   product.put("id", 1);
	   product.put("quantity", 1);
		  
	   Response addBasketResponse = TestApiHelpers.addProductToBasket(product);
	   /* assertEquals(addBasketResponse.getStatusCode(), 201);  //check added product to basket */
	   
	   double totalPriceAfterAddBasket = TestApiHelpers.getBasketTotalPrice(1);
	   assertEquals(totalPriceAfterAddBasket, ProductPrice - cargoPrice);
  } 

  //******************************************************	PRODUCT TAX CONTROL ************************************************************
  
  
  @Test
  public static void taxCalculate_ProductTaxCalculate_IsEquals() {   
	    double productTaxRate = TestApiHelpers.getProductTax(1); //20
	    double productPrice = TestApiHelpers.getProductPrice(1); //6000
	    double productTaxFree = TestApiHelpers.getProductTaxFree(1);  //5000
	    
	    double productTax = productPrice * productTaxRate / 100;    //1000 = 6000 * 20 / 100
	    
	    assertEquals(productPrice, productTax + productTaxFree);  //6000 = 1000 + 5000?
	  }
  
  @Test
  public static void taxCalculate_ElectronicProductTaxCalculate_IsEquals() {   
	    double productPrice = TestApiHelpers.getProductPrice(1);  //6000
	    double productTaxFree = TestApiHelpers.getProductTaxFree(1); //5000
	    
	    double productTax = productPrice * 20 / 100;   //1000 = 6000 * 20 /100
	    
	    assertEquals(productPrice, productTax + productTaxFree);  //6000 = 1000 + 5000?
	  }
  
  @Test
  public static void taxCalculate_FashionProductTaxCalculate_IsEquals() {    //dress
	    double productPrice = TestApiHelpers.getProductPrice(7);  //6000
	    double productTaxFree = TestApiHelpers.getProductTaxFree(7); //5400
	    
	    double productTax = productPrice * 10 / 100;   //600 = 6000 * 10 /100
	    
	    assertEquals(productPrice, productTax + productTaxFree);  //6000 = 600 + 5400?
	  }
  
  
  //******************************************************	NOTIFIED CONTROL ************************************************************
  
  
  @Test
  public static void GetNotifiedWhenProductPriceUpdateInBasket_IsEquals() {   
	  	JSONObject product = new JSONObject();
	  	
		   product.put("id", 1);
		   product.put("quantity", 1);
		   
	  	Response addBasketResponse = TestApiHelpers.addProductToBasket(product);
	  	/* assertEquals(addBasketResponse.getStatusCode(), 201);  //check added product to basket */
	  	
	    updateProductPrice(1, 20); 
	    
	    String basketContentsResponse = RestAssured.when()
                .get("/carts/user/5")
                .then()
                .extract().path("notification.message");
	    
	    assertTrue(basketContentsResponse.contains("Product price has been updated."));  //if have message, test passed
	  }
  
  private static void updateProductPrice(int productId, double newPrice) {
      JSONObject productUpdate = new JSONObject();
      productUpdate.put("id", productId);
      productUpdate.put("price", newPrice);

      RestAssured.given()
              .header("Content-Type", "application/json")
              .contentType(ContentType.JSON)
              .body(productUpdate)
              .when()
              .put("/products/" + productId);
  }
 
}