package jp.spring.boot.algolearn.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TeacherControllerTest {

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
    public void 教員用課題登録ページGET要求() throws Exception {
  	  
        mockMvc.perform(get("/teacher/task/add"))
            .andExpect(status().isOk());
    }

    @Test
    public void 教員用課題編集ページPOST要求() throws Exception {
  	  
        mockMvc.perform(post("/teacher/task/edit"))
            .andExpect(status().isOk());
    }

    @Test
    public void 教員用個人課題一覧ページGET要求() throws Exception {
  	  
        mockMvc.perform(get("/teacher/task/personallist"))
            .andExpect(status().isOk());
    }


    @Test
    public void 教員用公開課題一覧ページGET要求() throws Exception {
  	  
        mockMvc.perform(get("/teacher/task/publiclist"))
            .andExpect(status().isOk());
    }


}
