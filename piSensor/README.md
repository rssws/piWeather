# piSensor

This folder contains code running on `Raspberry Pi` with `DHT11` sensor.

The collected `humidity` and `temperature` are sent to `piService` and shown in `piClient`.

Theoretically, you can use any devices with any sensors.
The only thing you have to do is to send a `PUT` request to your `piService` instance.

`Put` request URL:

```
http://localhost:31414/api/local-weather/<name>/<PI_API>
```

Payload:

```json
{
  "description": "<name>",
  "temp": 25.2,
  "humidity": 51.6
}
```

## How to use

For hardware related information, please refer to [Freenove DHT11](https://github.com/Freenove/Freenove_Ultimate_Starter_Kit_for_Raspberry_Pi/tree/master/Code/Python_Code/21.1.1_DHT11) and [Freenove_Ultimate_Starter_Kit_for_Raspberry_Pi](https://github.com/Freenove/Freenove_Ultimate_Starter_Kit_for_Raspberry_Pi) as it is used in my set up.

In principle, any DHT11 or even DHT22 shuold work with the script.

1. Open the `.env` file in `piSensor` folder.
2. For now, `NAME` needs to be `home` as it is hardcoded in `piClient`.
3. Specify the `PI_SERVICE_URL` to your `piService` instance.
   - For example, via the nginx gateway `http://localhost:31414/api/` or directly to `http://localhost:31415/`.
4. Specify the `PI_API`. The `PUT` request can only be made sucessfully with a correct `PI_API` key.
   - It is the same key as the one in `.env` in the root folder when you execute `docker compose`.
   - Keep this key secret as anyone with this key can update the weather data.
