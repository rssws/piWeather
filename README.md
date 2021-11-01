# piWeather
*piWeather* is a project providing users with the weather information.
It consists of *piClient* as the frontend and *piService* as the backend.
Its initial purpose is to show weather on a monitor installed on raspberry pi.

## piClient
*piClient* is the frontend written in TypeScript and based on Angular.
Although, the pages are dynamically adjusted to fit the device resolution,
it might look better if using landscape layout.

Demo: [https://pi.zhongpin.wang](https://pi.zhongpin.wang)

## piService
*piService* serves as the backend of the project using Java Spring Boot.
It provides services including:
- current weather
- 7-day weather forecast
- ip geolocation

Some third-party APIs are used in this project:
- openweathermap.org
- ip-api.com

They both provide free plans for non-commercial use. Thanks a lot for that.

### Disclaimer:

The project is not built for commercial purposes.
The demo server is used for demonstration purposes only.

Please do not abuse the service of any kind,
though precautions have been made to prevent malicious requests.
The IP address will be recorded to limit the request frequency.
The results of requests will be cached for a pre-defined lifecycle 
in order to minimize the number of calls sent to the third-party APIs.

Please pay for the commercial plans of the above-listed services if used for business.
