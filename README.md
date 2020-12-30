# piService
API services, including weather, ipGeolocation, etc. Provided with the original purpose for the raspberry pi device. 

If you have any interest on the frontend, please refer to [https://github.com/rssws/piClient](https://github.com/rssws/piClient).

This backend service is built with Java Spring Boot. It provides services including:
 - current weather
 - 7-day weather forecast
 - ip geolocation
 
Some 3-rd party APIs are used in this project:
 - openweathermap.org
 - ip-api.com
 
Please notice that this project is not for commercial use. 
If you have intention to fork it for further development, 
pay attention to the legal notices of all services that you might want to include. 
For commercial use, you might have to apply your purchased api-key for corresponding services.

All the requests to the server are cached for a pre-defined lifecycle to minimize the amount of calls to 3-rd party APIs.
