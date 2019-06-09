package jp.spring.boot.algolearn.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jp.spring.boot.algolearn.AlgoLearnApplication;
import jp.spring.boot.algolearn.config.AceProperties;
import jp.spring.boot.algolearn.controller.StudentLearnController;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = AlgoLearnApplication.class)
//@ContextConfiguration(classes = AcePropertiesConfig.class)
public class StudentLearnControllerTest {

    @Autowired
    private MockMvc mockMvc;
	
    @Before
    public void before() throws Exception {
    	// Thymeleafを使用していることがテスト時に認識されない？
    	// 循環ビューが発生しないことを明示するためにViewResolverを使用
    	InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    	viewResolver.setPrefix("/templates");
    	viewResolver.setSuffix(".html");
    	mockMvc = MockMvcBuilders.standaloneSetup(new StudentLearnController()).setViewResolvers(viewResolver).build();
    }
  @Test
  public void 学生用学習ページGET要求正常() throws Exception {
  	
//      mockMvc.perform(get("/student/learn"))
//          .andExpect(status().isOk());
  }
}
