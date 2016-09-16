package uk.me.ruthmills.batbox.thermostat.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import uk.me.ruthmills.batbox.thermostat.model.TemperatureAndHumidityBean;
import uk.me.ruthmills.batbox.thermostat.service.ThermostatService;

@Service
public class ThermostatServiceImpl implements ThermostatService {

	@Override
	public TemperatureAndHumidityBean readTemperatureAndHumidity() {
		TemperatureAndHumidityBean temperatureAndHumidityBean = new TemperatureAndHumidityBean();
		
		InputStream stdout = null;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("/usr/local/bin/loldht", "8", "5");
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			stdout = process.getInputStream();
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			do
			{
				line = reader.readLine();
				System.out.println("Line: " + line);
				if ((line != null) && line.startsWith("Humidity")) {
					String humidity = line.substring("Humidity = ".length(), line.indexOf("%") - 1);
					String temperature = line.substring(line.indexOf("Temperature = ") + "Temperature = ".length(), line.indexOf("*") - 1);
					System.out.println("Humidity: <" + humidity + ">");
					System.out.println("Temperature: <" + temperature + ">");
					temperatureAndHumidityBean.setHumidity(new BigDecimal(humidity));
					temperatureAndHumidityBean.setTemperature(new BigDecimal(temperature));
				}
			} while (line != null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stdout != null) {
				try {
					stdout.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		return temperatureAndHumidityBean;
	}
}
