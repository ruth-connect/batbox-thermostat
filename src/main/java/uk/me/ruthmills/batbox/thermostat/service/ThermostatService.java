package uk.me.ruthmills.batbox.thermostat.service;

import uk.me.ruthmills.batbox.thermostat.model.TemperatureAndHumidityBean;

public interface ThermostatService {

	public TemperatureAndHumidityBean readTemperatureAndHumidity();
}
