package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController
{
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();
		return new ModelAndView("index", map);
	}
}
