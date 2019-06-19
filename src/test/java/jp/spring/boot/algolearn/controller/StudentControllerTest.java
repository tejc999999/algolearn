package jp.spring.boot.algolearn.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.Normalizer.Form;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jp.spring.boot.algolearn.form.LearnForm;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    WebApplicationContext wac;
	
    @Before
    public void before() throws Exception {
    	// Thymeleafを使用していることがテスト時に認識されない様子
    	// 循環ビューが発生しないことを明示するためにViewResolverを使用
    	InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    	viewResolver.setPrefix("/templates");
    	viewResolver.setSuffix(".html");
    	// StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
//    	mockMvc = MockMvcBuilders.standaloneSetup(new StudentLearnController()).setViewResolvers(viewResolver).build();
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
  @Test
  public void 学生用学習ページGET要求正常() throws Exception {
	  
      mockMvc.perform(get("/student/learn"))
          .andExpect(status().isOk());
  }
  
  @Test
  public void 学生用学習ページGET要求時view確認() throws Exception {
	  
      mockMvc.perform(get("/student/learn"))
          .andExpect(status().isOk())
          .andExpect(view().name(is("student/learn")));
  }
  
  @Test
  public void Java課題実行時POST要求正常() throws Exception {
	  
	  LearnForm form = new LearnForm();
	  String code = "public class Test { public static void main(String[] args) { }}";
	  form.setCode(code);
	  form.setCodeHead("");
	  form.setProgrammingLanguage("java");
	  
      mockMvc.perform(post("/student/execute").flashAttr("studentLearnForm",form))
          .andExpect(status().isOk());
  }
  
  @Test
  public void Java課題実行時POST要求時view確認() throws Exception {
	  
	  LearnForm form = new LearnForm();
	  String code = "public class Test { public static void main(String[] args) { }}";
	  form.setCode(code);
	  form.setCodeHead("");
	  form.setProgrammingLanguage("java");
	  
      mockMvc.perform(post("/student/execute").flashAttr("studentLearnForm",form))
          .andExpect(status().isOk())
          .andExpect(view().name(is("student/learn")));
  }
}