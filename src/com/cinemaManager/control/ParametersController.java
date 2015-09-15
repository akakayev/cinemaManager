package com.cinemaManager.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinemaManager.config.JSONConfigParser;

@Controller
public class ParametersController {

	@RequestMapping(value = "/ReportGenerator", method = RequestMethod.GET)
	public String report() {
		return "ReportGenerator";
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public @ResponseBody String generate(@RequestParam(value = "parameters") String json) {
		JSONConfigParser parser=new JSONConfigParser(json);
		ReportController executor = new ReportController(parser.parse());
		return executor.getReportFilePath();
	}
}
