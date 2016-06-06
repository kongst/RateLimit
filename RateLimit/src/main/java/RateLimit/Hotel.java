package RateLimit;

public class Hotel implements Comparable<Hotel>  {
	
	public Hotel() {
	}
	public Hotel(String city, int hotelID, String room, int price) {
		this.city = city;
		this.hotelID = hotelID;		
		this.room = room;
		this.price = price;
	}
		
	public int getHotelID() {
		return hotelID;
	}
	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String toString() {
		String result = "{\"city\": \"" + city + "\", "
		 + "\"hotelid\": \"" + hotelID + "\", "
		 + "\"room\": \"" + room + "\", "
		 + "\"price\": \"" + price + "\"}";
		
		return result;
	}
	
	@Override
	public int compareTo(Hotel hotel) {
		return this.price - hotel.price;
	}
	
	
	private int hotelID;
	private String city;
	private String room;
	private int price;

}
