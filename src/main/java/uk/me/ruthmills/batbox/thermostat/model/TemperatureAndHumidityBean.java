package uk.me.ruthmills.batbox.thermostat.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TemperatureAndHumidityBean implements Serializable {
	private static final long serialVersionUID = 8740215093982572506L;
	
	private BigDecimal temperature;
	private BigDecimal humidity;
	
	public BigDecimal getTemperature() {
		return temperature;
	}
	
	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getHumidity() {
		return humidity;
	}

	public void setHumidity(BigDecimal humidity) {
		this.humidity = humidity;
	}
}
