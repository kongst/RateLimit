# RateLimit

RateLimit application is a web service, using to query hotel by city. It take 3 parameters as follows
- key: specify api key.
- city: specify hotel's city (case sensitive)
- sort: specify price sort. The possible value are asc, desc and none

This webservice is rate limit which can be configure at \RateLimit\config\application.properties

Example:

http://localhost:8080/ratelimit?key=key1&city=Bangkok&sort=asc

Result:

{"result": [{"city": "Bangkok", "hotelid": "11", "room": "Deluxe", "price": "60"},{"city": "Bangkok", "hotelid": "15", "room": "Deluxe", "price": "900"},{"city": "Bangkok", "hotelid": "1", "room": "Deluxe", "price": "1000"},{"city": "Bangkok", "hotelid": "6", "room": "Superior", "price": "2000"},{"city": "Bangkok", "hotelid": "8", "room": "Superior", "price": "2400"},{"city": "Bangkok", "hotelid": "18", "room": "Sweet Suite", "price": "5300"},{"city": "Bangkok", "hotelid": "14", "room": "Sweet Suite", "price": "25000"}]}
