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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class ThermostatServiceImpl implements ThermostatService {
	
	private static final String LOLDHT_PATH = "/usr/local/bin/loldht";
	private static final String GPIO_PORT = "8";
	private static final String NUM_RETRIES = "10";
	
	private static final Logger LOGGER = Logger.getLogger(ThermostatServiceImpl.class);

	private TemperatureAndHumidityBean temperatureAndHumidity;
	private boolean shutdown;

	@PostConstruct
	public void initialise() {
		Runnable runnable = new ThermostatRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	@PreDestroy
	public void shutdown() {
		shutdown = true;
	}

	@Override
	public TemperatureAndHumidityBean readTemperatureAndHumidity() {
		return temperatureAndHumidity;
	}

	private class ThermostatRunnable implements Runnable {

		@Override
		public void run() {
			while (!shutdown) {
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
				temperatureAndHumidity = temperatureAndHumidityBean;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					LOGGER.error("Interrupted Exception", ex);
				}
			}
		}
	}
}
