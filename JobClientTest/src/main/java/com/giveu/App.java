package com.giveu;

import com.alibaba.fastjson.JSONObject;
import com.giveu.com.giveu.model.TestModel;
import com.giveu.com.giveu.service.LogTestService;
import com.giveu.common.httpclient.component.HttpComponent;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.job.scan.annotation.JobCallBackSignValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

/**
 * Created by fox on 2018/6/25.
 */
@RestController
@SpringBootApplication
@Configuration
public class App {

	@Autowired
	LogTestService logTestService;

	@Autowired
	HttpComponent httpComponent;


//	@JobCallBackSignValid
//	@RequestMapping("sign")
//	public String sign() {
//		String json = "{\"name\":\"hanbin\", \"age\":12}";
//		return json;
//
//	}
	@JobCallBackSignValid
	@RequestMapping("sign")
	@ResponseBody
	public TestModel sign() {
		System.out.println("sign...");
		TestModel model = new TestModel();
		model.setCode("200");
		model.setMessage("succcccc");
		model.setData("xxx");
		model.setStatus(2);
		return model;
	}



	@RequestMapping("async")
	public void async(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
		String sessionId = request.getParameter("jobSessionId");
		System.out.println(sessionId);
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		String resultJson = JSONObject.toJSONString(resultModel);
		response.setContentType("application/json;charset=utf-8");
		Writer writer = response.getWriter();
		writer.write(resultJson);
		writer.flush();
		writer.close();
		System.out.println("wait...");

//		Thread.sleep(50000);
//
//		String url = "http://localhost:9060/job/result/update";
//
//		Map<String, String> map = new HashMap<>();
//		map.put("sessionId", sessionId);
//		map.put("executeStatus", "2");
//
//		ResultModel result = httpComponent.doPostFormResultObject(url, ResultModel.class, map);
//		System.out.println(result);
	}
	@RequestMapping("async/result")
	public ResultModel asyncResult() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		return resultModel;
	}

//	@RequestMapping("foo1")
//	public ResultModel foo1(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
//		logTestService.foo1();
//		ResultModel resultModel = new ResultModel();
//		resultModel.setCode(200);
//		resultModel.setMessage("ok");
//		return resultModel;
//	}

	@RequestMapping("foo2")
	public ResultModel foo2(HttpServletResponse response) throws InterruptedException, IOException {
//		logTestService.foo2();
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		resultModel.setMessage("ok");
		return resultModel;
	}

	@RequestMapping("foo3")
	public ResultModel foo3(HttpServletResponse response) throws InterruptedException, IOException {
		logTestService.foo3();
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		resultModel.setMessage("ok");
		return resultModel;
	}




	@RequestMapping("job")
	public ResultModel jobRun(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
//		Thread.sleep(10*1000);
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		resultModel.setMessage("ok");
		resultModel.setData(request.getParameter("jobSessionId"));
		System.out.println("Job Executor...");
		return resultModel;
	}


	@RequestMapping("foo")
	public void jobFoo(HttpServletResponse response) throws InterruptedException, IOException {
		System.out.println(new Date());
		int x = (int)(Math.random()*1000);
//		Thread.sleep(x);

		String nowTime = new Date().getTime()+" || ";
		response.getWriter().write(nowTime);
		response.getWriter().flush();
		response.getWriter().close();



//		Thread.sleep(20*1000);

		System.out.println("xxxxxx");
		String nowTime2 = new Date().getTime()+"";
		System.out.println(nowTime2);

//		return "success";
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}