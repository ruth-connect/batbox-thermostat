# batbox-thermostat
Spring Boot software to run on a Raspberry Pi 3 to read from a DHT-22 temperature and humidity sensor

Needs loldht to be installed, as it will execute loldht to read the temperature and humidity, and parse the result.

http://localhost:61453/thermostat/readTemperatureAndHumidity

will return temperature and humidity in JSON format, e.g.

{"temperature":21.5,"humidity":33.6}
