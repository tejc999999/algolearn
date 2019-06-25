package jp.spring.boot.algolearn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
