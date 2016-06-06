package RateLimit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HotelController {

	public static final String SORT_NONE = "none";
	public static final String SORT_ASC  = "asc";
	public static final String SORT_DESC = "desc";
	
	public HotelController() {
		loadHotelDB();
		initialHotelByCity();	
		// Pre-sorting to reduce processing time at run time
		initialHotelByCityWithPriceSort();
	}

	private void loadHotelDB() {
		String csvFile = "hoteldb.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				if (isHeader == false) {
					String[] out = line.split(cvsSplitBy);

					if (out.length == 4) {
						Hotel hotel = new Hotel();
						hotel.setCity(out[0]);
						hotel.setHotelID(Integer.parseInt(out[1]));
						hotel.setRoom(out[2]);
						hotel.setPrice(Integer.parseInt(out[3]));

						hotelList.add(hotel);
					}
				} else {
					isHeader = false;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void initialHotelByCity() {

		for (int i = 0; i < hotelList.size(); i++) {
			Hotel hotel = hotelList.get(i);

			ArrayList<Hotel> hotelInCityList = hotelByCity.get(hotel.getCity());

			if (hotelInCityList == null) {

				hotelInCityList = new ArrayList<Hotel>();

				hotelByCity.put(hotel.getCity(), hotelInCityList);
			}

			hotelInCityList.add(hotel);
		}
	}

	private void initialHotelByCityWithPriceSort() {
		// Sort hotel by price asc in each city
		for (String key : hotelByCity.keySet()) {
			ArrayList<Hotel> hotelList = hotelByCity.get(key);

			ArrayList<Hotel> hotelListPriceSortAsc = new ArrayList<Hotel>(hotelList);
			Collections.sort(hotelListPriceSortAsc);
			hotelByCityWithPriceSort.put(key, hotelListPriceSortAsc);
		}
	}

	public String getHotelbyCity(String city, String sortType) {
		
		String result = new String();

		result = "{\"result\": [";

		if (sortType.equals(SORT_NONE)) {
			// Non-sorting using hotelByCity
			ArrayList<Hotel> hotelList = hotelByCity.get(city);

			if (hotelList != null) {
				for (int i = 0; i < hotelList.size(); i++) {
					Hotel hotel = hotelList.get(i);
					result += hotel.toString();

					if (i < hotelList.size() - 1) {
						result += ",";
					}
				}
			}
		} else {
			ArrayList<Hotel> hotelList = hotelByCityWithPriceSort.get(city);

			if (hotelList != null) {
				if (sortType.equals(SORT_ASC)) { 
					// Iterate from 0 to size() for sort ASC
					for (int i = 0; i < hotelList.size(); i++) {
						Hotel hotel = hotelList.get(i);
						result += hotel.toString();

						if (i < hotelList.size() - 1) {
							result += ",";
						}
					}
				} else if (sortType.equals(SORT_DESC)) {
					// Iterate from size() to 0 for sort DESC
					for (int i = hotelList.size() - 1; i >= 0; i--) {
						Hotel hotel = hotelList.get(i);
						result += hotel.toString();

						if (i >= 1) {
							result += ",";
						}
					}
				}
			}
		}
		
		result += "]}";
		
		return result;
	}
	
	// List of hotel from hoteldb.csv without sorting.
	ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
	
	// Map city to hotel list
	Map<String, ArrayList<Hotel>> hotelByCity = new HashMap<String, ArrayList<Hotel>>();	
	
	// Map city to hotel list with ASC sorting
	Map<String, ArrayList<Hotel>> hotelByCityWithPriceSort = new HashMap<String, ArrayList<Hotel>>();
}
