package uk.me.ruthmills.batbox.thermostat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.me.ruthmills.batbox.thermostat.model.TemperatureAndHumidityBean;
import uk.me.ruthmills.batbox.thermostat.service.ThermostatService;

@Controller
@RequestMapping("/thermostat")
public class ThermostatController {
	
	@Autowired
	private ThermostatService thermostatService;

	@RequestMapping(value = "/readTemperatureAndHumidity", method = RequestMethod.GET)
	public @ResponseBody TemperatureAndHumidityBean readTemperatureAndHumidity() {
		return thermostatService.readTemperatureAndHumidity();
	}
}
