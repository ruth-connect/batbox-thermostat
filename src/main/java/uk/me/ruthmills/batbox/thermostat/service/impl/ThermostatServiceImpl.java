package uk.me.ruthmills.batbox.thermostat.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.batbox.thermostat.model.TemperatureAndHumidityBean;
import uk.me.ruthmills.batbox.thermostat.service.ThermostatService;

@Service
public class ThermostatServiceImpl implements ThermostatService {
	
	private static final String LOLDHT_PATH = "/usr/local/bin/loldht";
	private static final String GPIO_PORT = "8";
	private static final String NUM_RETRIES = "5";
	
	private static final Logger LOGGER = Logger.getLogger(ThermostatServiceImpl.class);

	@Override
	public TemperatureAndHumidityBean readTemperatureAndHumidity() {
		return invokeLoldht();
	}
	
	private synchronized TemperatureAndHumidityBean invokeLoldht() {
		TemperatureAndHumidityBean temperatureAndHumidityBean = new TemperatureAndHumidityBean();
		
		InputStream stdout = null;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(LOLDHT_PATH, GPIO_PORT, NUM_RETRIES);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			stdout = process.getInputStream();
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			do
			{
				line = reader.readLine();
				LOGGER.trace("Line: " + line);
				if ((line != null) && line.startsWith("Humidity")) {
					String humidity = line.substring("Humidity = ".length(), line.indexOf("%") - 1);
					String temperature = line.substring(line.indexOf("Temperature = ") + "Temperature = ".length(), line.indexOf("*") - 1);
					LOGGER.debug("Humidity: <" + humidity + ">");
					LOGGER.debug("Temperature: <" + temperature + ">");
					temperatureAndHumidityBean.setHumidity(new BigDecimal(humidity));
					temperatureAndHumidityBean.setTemperature(new BigDecimal(temperature));
				}
			} while (line != null);
		} catch (Exception ex) {
			LOGGER.error(ex);
		} finally {
			if (stdout != null) {
				try {
					stdout.close();
				} catch (IOException ex) {
					LOGGER.error(ex);
				}
			}
		}
		
		return temperatureAndHumidityBean;		
	}
}
