package RateLimit;

import java.util.HashMap;
import java.util.Map;
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
    	initialAPIKey();
    }
    
    private void initialAPIKey() {
    	APIKey apikey1 = new APIKey("key1");
    	APIKey apikey2 = new APIKey("key2");
    	APIKey apikey3 = new APIKey("key3");
    	APIKey apikey4 = new APIKey("key4");
    	
    	apiKeyMap.put("key1", apikey1);
    	apiKeyMap.put("key2", apikey2);
    	apiKeyMap.put("key3", apikey3);
    	apiKeyMap.put("key4", apikey4);
    }
    
    @RequestMapping("/ratelimit")
	public String ratelimit(@RequestParam(value = "key", defaultValue = "") String key,
			@RequestParam(value = "city", defaultValue = "") String city,
			@RequestParam(value = "sort", defaultValue = "none") String sort) {
    	
		APIKey apiKey = apiKeyMap.get(key);
		String result = new String();
		
		if (apiKey != null) {	
			
			long currentTime = System.currentTimeMillis();
			
			if (validateRateLimit(apiKey,currentTime)) {
				result = hotelController.getHotelbyCity(city, sort);						
			} else {
				result = "{\"result\": \"limit exceeded\"}";
			}
			
		} else {
			result = "{\"result\": \"invalid key\"}";
		}
		
		return result;
	}
    
	public synchronized boolean validateRateLimit(APIKey apiKey, long currentTime) {
		
		if (apiKey.getSuspendedStartTime() == 0) {
			
			long currentRateLimitTime = currentTime - apiKey.getRateLimitStartTime();
	
			if (currentRateLimitTime > ratelimit_time) { // Passed rate limit time
				apiKey.setRateLimitCount(1);
				apiKey.setRateLimitStartTime(currentTime);
				return true;
			} else { // in rate limit time
				apiKey.increaseRateLimitCount();

				if (apiKey.getRateLimitCount() <= ratelimit_limit) {
					return true;
				} else {
					apiKey.setSuspendedStartTime(currentTime);
					return false;
				}
			}

		} else { // Suspended
			
			long currentSuspendedTime = currentTime - apiKey.getSuspendedStartTime();
				
			if (currentSuspendedTime > suspendedTime) { // Passed suspended time
				apiKey.setRateLimitCount(1);
				apiKey.setRateLimitStartTime(currentTime);
				apiKey.setSuspendedStartTime(0);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void setRatelimit_limit(int ratelimit_limit) {
		this.ratelimit_limit = ratelimit_limit;
	}
	
	public int getRatelimit_limit() {
		return this.ratelimit_limit;
	}
	
	public void setRatelimit_time(long ratelimit_time) {
		this.ratelimit_time = ratelimit_time;
	}
	
	public long getRatelimit_time() {
		return this.ratelimit_time;
	}
	
	public void setSuspendedTimee(long suspendedTime) {
		this.suspendedTime = suspendedTime;
	}
	
	public long getSuspendedTime() {
		return this.suspendedTime;
	}
}

