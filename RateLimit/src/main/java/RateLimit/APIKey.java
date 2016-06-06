package RateLimit;


public class APIKey {
	
	public APIKey(String apiKey) {
		this.apiKey = apiKey;
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

	private String apiKey;
	private int rateLimitCount;
	private long rateLimitStartTime;
	private long suspendedStartTime;
	
}
