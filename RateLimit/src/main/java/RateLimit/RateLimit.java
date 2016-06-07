package RateLimit;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Component
public class RateLimit {
	
    @Value("${ratelimit.limit:1}")
    private int ratelimit_limit; // number of request per rate limit time (default = 1 request / 10 second)
	
	@Value("${ratelimit.time:10000}")
	private long ratelimit_time; // rate limit time (default = 1 request / 10 second)
    
    @Value("${suspendedTime:300000}")
    private long suspendedTime; // suspended time in millisecond (default = 5 minutes) 
    
    private HotelController hotelController = new HotelController();    
    private Map<String, APIKey> apiKeyMap = new HashMap<String, APIKey>();	
	
    public RateLimit() {    
   
    }
    
    private void initialAPIKey() {
    	APIKey apikey1 = new APIKey("key1", ratelimit_limit, ratelimit_time, suspendedTime);
    	APIKey apikey2 = new APIKey("key2", ratelimit_limit, ratelimit_time, suspendedTime);
    	APIKey apikey3 = new APIKey("key3", ratelimit_limit, ratelimit_time, suspendedTime);
    	APIKey apikey4 = new APIKey("key4", ratelimit_limit, ratelimit_time, suspendedTime);
    	
    	apiKeyMap.put("key1", apikey1);
    	apiKeyMap.put("key2", apikey2);
    	apiKeyMap.put("key3", apikey3);
    	apiKeyMap.put("key4", apikey4);
    }
    
    @PostConstruct
    public void init() {
    	initialAPIKey();
    }
    
    @RequestMapping("/ratelimit")
	public String ratelimit(@RequestParam(value = "key", defaultValue = "") String key,
			@RequestParam(value = "city", defaultValue = "") String city,
			@RequestParam(value = "sort", defaultValue = "none") String sort) {
    	
		APIKey apiKey = apiKeyMap.get(key);
		String result = new String();
		
		if (apiKey != null) {	
			
			long currentTime = System.currentTimeMillis();
			
			if (apiKey.validateRateLimit(currentTime)) {
				result = hotelController.getHotelbyCity(city, sort);						
			} else {
				result = "{\"result\": \"limit exceeded\"}";
			}
			
		} else {
			result = "{\"result\": \"invalid key\"}";
		}
		
		return result;
	}
}

