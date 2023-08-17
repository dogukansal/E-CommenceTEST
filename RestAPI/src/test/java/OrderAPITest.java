import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class OrderAPITest {
	
	private String baseurl = "https://dummyjson.com";
    private JSONObject cardInfo;
    private JSONArray userInfoArray;
    private JSONArray cardInfoArray;
    private JSONObject mainObject;
	public static void main(String[] args) {
		

	}
		
	 	@BeforeClass
	    public void setUp() {
	 		
	 		cardInfo = new JSONObject();
	 		cardInfo.put("address", "abc");
	 		cardInfo.put("cardNumber", 1234);
	 		cardInfo.put("expirationDate", "invalid-card");
	 		cardInfo.put("CVVDate", "invalid-card");
	 		
	 		cardInfoArray  = new JSONArray();
	 		cardInfoArray.add(cardInfo);
	        
	 		JSONObject userInfo = new JSONObject();
	        userInfo.put("userId", 1);
	        userInfo.put("userName", "a");
	        userInfo.put("userMail", "a");
	        userInfo.put("userPhoneNumber", 1);
	        userInfo.put("userAddress", "a");
	        
	        userInfoArray = new JSONArray();
	        userInfoArray.add(userInfo);
	        
	        mainObject = new JSONObject();
	        mainObject.put("userInfo", userInfoArray);
	        mainObject.put("cardInfo", cardInfoArray);
	        mainObject.put("status", "a");
	    }
		 	//"userInfo" : [	{
		 	//"userId":1,
		 	//"userName":"a",
		 	//"userMail":"a",
		 	//"userPhoneNumber":1,
		 	//"userAddress":"a", }  ]
		 	
		    //"cardInfo" : [	{
		    //"cardNumber": 77,
		    //"expirationDate": 1,  
		 	//"CVVDate": 1  }  ]
	 		
	 		//"status" : "a",
	 		//
	    @Test
	    public void testPlaceOrderWithoutAddress() {
	    	mainObject.remove("userInfo[0].address"); // Adres bilgisi eksik
	        placeOrderAndVerifyResult(mainObject, "Address information missing.");
	    }

	    @Test
	    public void testPlaceOrderWithoutCardInfo() {
	    	mainObject.remove("cardInfo"); // Kart bilgisi eksik
	        placeOrderAndVerifyResult(mainObject, "Card information missing.");
	    }

	    @Test
	    public void testPlaceOrderWithInvalidCard() {
	    	mainObject.put("cardInfo[0].cardNumber", "invalid-card"); // Geçersiz kart bilgisi
	        placeOrderAndVerifyResult(mainObject, "Invalid card information.");
	    }
	    
	    @Test
	    public void testPlaceOrderWithInvalidCard2() {
	    	mainObject.put("cardInfo[0].expirationDate", "1"); // Geçersiz kart bilgisi
	        placeOrderAndVerifyResult(mainObject, "Invalid card information.");
	    }
	    @Test
	    public void testPlaceOrderCancelledByBank() {
	    	mainObject.put("status", "cancelled");
	        placeOrderAndVerifyResult(mainObject, "Order cancelled by the bank.");
	    }

	    private void placeOrderAndVerifyResult(JSONObject payload, String expectedMessage) {
	        RestAssured
	    	.given()
	            .contentType(ContentType.JSON)
	            .body(payload.toString())
	        .when()
	            .post(baseurl + "/orders/place")
	        .then()
	            .statusCode(200)
	            .body("message", equalTo(expectedMessage));
	    }

}
