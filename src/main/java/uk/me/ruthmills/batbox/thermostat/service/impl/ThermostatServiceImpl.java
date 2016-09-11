package uk.me.ruthmills.batbox.thermostat.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import uk.me.ruthmills.batbox.thermostat.model.TemperatureAndHumidityBean;
import uk.me.ruthmills.batbox.thermostat.service.ThermostatService;

@Service
public class ThermostatServiceImpl implements ThermostatService {

	@Override
	public TemperatureAndHumidityBean readTemperatureAndHumidity() {
		TemperatureAndHumidityBean temperatureAndHumidityBean = new TemperatureAndHumidityBean();
		temperatureAndHumidityBean.setTemperature(new BigDecimal("21.5"));
		temperatureAndHumidityBean.setHumidity(new BigDecimal("33.6"));
		return temperatureAndHumidityBean;
	}
}
