#!/usr/bin/env python3
#############################################################################
# Filename    : DHT11.py
# Description :	read the temperature and humidity data of DHT11
# Author      : freenove
# modification: 2020/10/16
########################################################################
import RPi.GPIO as GPIO
import time
import DHT11
import requests
import os
from dotenv import load_dotenv

PI_SERVICE_URL = 'http://localhost:31415/'
NAME = 'default'
PI_API = ''

DHTPin = 11     #define the pin of DHT11

def sendData(humidity, temperatur):
    payload = { 'description': NAME, 'humidity': humidity, 'temp': temperatur }
    requests.put(PI_SERVICE_URL + 'local-weather/' + NAME + '/' + PI_API, json=payload)

def loop():
    dht = DHT11.DHT(DHTPin)   #create a DHT class object
    counts = 0 # Measurement counts
    while True:
        counts += 1
        # print("Measurement counts: ", counts)
        for i in range(0, 15):            
            chk = dht.readDHT11()     #read DHT11 and get a return value. Then determine whether data read is normal according to the return value.
            if (chk is dht.DHTLIB_OK):      #read DHT11 and get a return value. Then determine whether data read is normal according to the return value.
                # print("DHT11,OK!")
                break
            time.sleep(0.1)
        sendData(dht.humidity, dht.temperature)
        print("Humidity : %.2f, \t Temperature : %.2f \n" % (dht.humidity, dht.temperature))
        time.sleep(5)

if __name__ == '__main__':
    load_dotenv()
    NAME = os.getenv('NAME')
    PI_SERVICE_URL = os.getenv('PI_SERVICE_URL')
    PI_API = os.getenv('PI_API')

    try:
        loop()
    except KeyboardInterrupt:
        GPIO.cleanup()
        exit()  
