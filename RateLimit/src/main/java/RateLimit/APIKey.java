package RateLimit;


public class APIKey {
	
	public APIKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	
	public APIKey(String apiKey, int ratelimit_limit, long ratelimit_time, long suspendedTime) {
		this.apiKey = apiKey;
		this.ratelimit_limit = ratelimit_limit;
		this.ratelimit_time = ratelimit_time;
		this.suspendedTime = suspendedTime;
	}


	public String getApiKey() {
		return apiKey;
	}


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


	public long getRateLimitStartTime() {
		return rateLimitStartTime;
	}



	public void setRateLimitStartTime(long starTime) {
		this.rateLimitStartTime = starTime;
	}


	public int getRateLimitCount() {
		return rateLimitCount;
	}



	public void setRateLimitCount(int rateLimitCount) {
		this.rateLimitCount = rateLimitCount;
	}

	public void increaseRateLimitCount() {
		this.rateLimitCount++;
	}

	public long getSuspendedStartTime() {
		return suspendedStartTime;
	}

	public void setSuspendedStartTime(long suspensedStartTime) {
		this.suspendedStartTime = suspensedStartTime;
	}
	
	
	public synchronized boolean validateRateLimit(long currentTime) {
		
		if (getSuspendedStartTime() == 0) {
			
			long currentRateLimitTime = currentTime - getRateLimitStartTime();
	
			if (currentRateLimitTime > ratelimit_time) { // Passed rate limit time
				setRateLimitCount(1);
				setRateLimitStartTime(currentTime);
				return true;
			} else { // in rate limit time
				increaseRateLimitCount();

				if (getRateLimitCount() <= ratelimit_limit) {
					return true;
				} else {
					setSuspendedStartTime(currentTime);
					return false;
				}
			}

		} else { // Suspended
			
			long currentSuspendedTime = currentTime - getSuspendedStartTime();
				
			if (currentSuspendedTime > suspendedTime) { // Passed suspended time
				setRateLimitCount(1);
				setRateLimitStartTime(currentTime);
				setSuspendedStartTime(0);
				return true;
			} else {
				return false;
			}
		}
	}

	private String apiKey;
	private int ratelimit_limit;
	private long ratelimit_time;
	private long suspendedTime;
	
	private int rateLimitCount;
	private long rateLimitStartTime;
	private long suspendedStartTime;
	
}
