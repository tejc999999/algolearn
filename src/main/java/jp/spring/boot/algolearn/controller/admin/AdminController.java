package jp.spring.boot.algolearn.controller.admin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.config.PrgLanguage;
import jp.spring.boot.algolearn.datasource.DynamicRoutingDataSourceResolver;
import jp.spring.boot.algolearn.datasource.SchemaContextHolder;
import jp.spring.boot.algolearn.datasource.SchemaType;
import jp.spring.boot.algolearn.form.LearnForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	
//	@GetMapping(path="setting")
//	public String setting(Model model) {
//		List<String> list = new ArrayList<String>();
//		for(PrgLanguage prgLanguage : PrgLanguage.values()) {
//			list.add(prgLanguage.toString());
//		}
//		
//		model.addAttribute("prgNameList", list);
//		
//		return "admin/setting";
//	}
//	
//	@PostMapping(path="setting")
//	public String setting(PrgLanguageForm form) {
//
//		
////		DataSource ds = DataSourceBuilder.create().url(form.getDatabaseUrl()).username(form.getDatabaseUsername()).password(form.getDatabasePassword()).driverClassName(form.getDatabaseDriver()).build();
////		DynamicRoutingDataSourceResolver resolver = new DynamicRoutingDataSourceResolver();
////
////		String databaseName = form.getDatabaseName();
////		if(SchemaType.H2DB.toString().equals(databaseName)) {
////			Map<Object, Object> datasources = new HashMap<Object,Object>();
////			datasources.put(SchemaType.H2DB.toString(), ds);
////	        resolver.setTargetDataSources(datasources);
////	        resolver.setDefaultTargetDataSource(ds);
////	        SchemaContextHolder.schemaType = SchemaType.H2DB;
////		} else if(SchemaType.MARIADB.toString().equals(databaseName)) {
////			Map<Object, Object> datasources = new HashMap<Object,Object>();
////			datasources.put(SchemaType.MARIADB.toString(), ds);
////	        resolver.setTargetDataSources(datasources);
////	        resolver.setDefaultTargetDataSource(ds);
////	        SchemaContextHolder.schemaType = SchemaType.MARIADB;
////		}
////		
////		long cnt = taskRepository.count();
////		
////		if(cnt == 0) {
////			TaskBean taskBean = new TaskBean();
////			taskBean.setId("000001");
////			taskBean.setTitle("title A");
////			taskRepository.save(taskBean);
////		} else {
////			taskRepository.deleteAll();
////		}
//		
//
//		return "admin/setting";
//	}
	
}
