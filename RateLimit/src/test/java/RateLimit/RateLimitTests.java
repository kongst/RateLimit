/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package RateLimit;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import RateLimit.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RateLimitTests {

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void testSearchCityNonSorting() throws Exception {

		this.mockMvc.perform(get("/ratelimit").param("key", "key1").param("city", "Bangkok").param("sort", "none"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.[0].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[0].price").value("1000"))
				.andExpect(jsonPath("$.result.[3].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[3].price").value("60"))
				.andExpect(jsonPath("$.result.[6].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[6].price").value("5300"));
	}
	
	@Test
	public void testSearchCitySortingAsc() throws Exception {

		this.mockMvc.perform(get("/ratelimit").param("key", "key2").param("city", "Bangkok").param("sort", "asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.[0].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[0].price").value("60"))
				.andExpect(jsonPath("$.result.[3].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[3].price").value("2000"))
				.andExpect(jsonPath("$.result.[6].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[6].price").value("25000"));
	}
	
	@Test
	public void testSearchCitySortingDesc() throws Exception {

		this.mockMvc.perform(get("/ratelimit").param("key", "key3").param("city", "Bangkok").param("sort", "desc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.[0].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[0].price").value("25000"))
				.andExpect(jsonPath("$.result.[3].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[3].price").value("2000"))
				.andExpect(jsonPath("$.result.[6].city").value("Bangkok"))
				.andExpect(jsonPath("$.result.[6].price").value("60"));
	}


	@Test
	public void testSearchCityWithInvalidKey() throws Exception {

		this.mockMvc.perform(get("/ratelimit").param("key", "keyNull").param("city", "Bangkok").param("sort", "desc"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value("invalid key"));
	}
	
	@Test
	public void testRateLimit() throws Exception {
		RateLimit rateLimit = new RateLimit();
		
		rateLimit.setRatelimit_time(3000);
		rateLimit.setRatelimit_limit(2);
		rateLimit.setSuspendedTimee(5000);
		
		APIKey apikey1 = new APIKey("key1");
		boolean result = false;
		
		// Rate limit ok
		result = rateLimit.validateRateLimit(apikey1, 1000);
		assertEquals(result, true);
		
		result = rateLimit.validateRateLimit(apikey1, 1100);
		assertEquals(result, true);
		
		result = rateLimit.validateRateLimit(apikey1, 4200);
		assertEquals(result, true);
		
		result = rateLimit.validateRateLimit(apikey1, 4300);
		assertEquals(result, true);
		
		// Rate limit exceeded
		result = rateLimit.validateRateLimit(apikey1, 4400);
		assertEquals(result, false);
		
		// Rate limit suspended
		result = rateLimit.validateRateLimit(apikey1, 9300);
		assertEquals(result, false);
		
		// Rate limit pass suspended time
		result = rateLimit.validateRateLimit(apikey1, 9500);
		assertEquals(result, true);
	}
}
