package jp.spring.boot.algolearn.controller;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TeacherQuestionControllerTest {

	@Autowired
    MockMvc mockMvc;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
    WebApplicationContext wac;
	
	@PersistenceContext
	EntityManager entityManager;
	
    @Before
    @Sql({"/schema-test.sql", "/data-test.sql"})
    public void テスト前処理() throws Exception {
    	// Thymeleafを使用していることがテスト時に認識されない様子
    	// 循環ビューが発生しないことを明示するためにViewResolverを使用
    	InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    	viewResolver.setPrefix("/templates");
    	viewResolver.setSuffix(".html");
    	// StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
//    	mockMvc = MockMvcBuilders.standaloneSetup(new StudentLearnController()).setViewResolvers(viewResolver).build();
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    	
    	// DBクリア
    	questionRepository.deleteAll();
    	
    }
    
    @Test
    public void 先生用問題一覧ページ表示() throws Exception {
  	  
    	QuestionBean bean1 = new QuestionBean();
    	bean1.setId(1);
    	bean1.setTitle("テストタイトル1");
    	bean1.setDescription("テスト説明1");
    	bean1.setInputNum(2);
    	bean1.setPublicFlg(true);
    	
    	QuestionBean bean2 = new QuestionBean();
    	bean2.setId(2);
    	bean2.setTitle("テストタイトル2");
    	bean2.setDescription("テスト説明2");
    	bean2.setInputNum(0);
    	bean2.setPublicFlg(false);
    	
    	questionRepository.save(bean1);
    	questionRepository.save(bean2);
    	
    	MvcResult result = mockMvc.perform(get("/teacher/question"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/list"))
            .andReturn();
    	
    	List<QuestionForm> list = (List) result.getModelAndView().getModel().get("questions");
    	
    	QuestionForm form1 = new QuestionForm();
    	BeanUtils.copyProperties(bean1, form1);
    	form1.setId(String.valueOf(bean1.getId()));
    	
    	QuestionForm form2 = new QuestionForm();
    	BeanUtils.copyProperties(bean2, form2);
    	form2.setId(String.valueOf(bean2.getId()));
    	
    	assertThat(list, hasItems(form1, form2));
    }
    
    @Test
    public void 先生用問題登録ページ表示() throws Exception {
  	  
        mockMvc.perform(get("/teacher/question/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/add"));
    }
    
    @Test
    public void 先生用問題登録処理() throws Exception {
  	  
    	QuestionForm form = new QuestionForm();
    	form.setTitle("テストタイトル");
    	form.setDescription("テスト説明");
    	form.setInputNum(2);
    	form.setPublicFlg(true);
    	
        mockMvc.perform(post("/teacher/question/add")
        		.flashAttr("questionForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"));
        
        Optional<QuestionBean> opt = questionRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(bean -> {

	    		assertEquals(bean.getTitle(), form.getTitle());
	    		assertEquals(bean.getDescription(),form.getDescription());
	    		assertEquals(bean.getInputNum(), form.getInputNum());
	    		assertEquals(bean.isPublicFlg(), form.isPublicFlg());
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用問題編集ページ表示() throws Exception {
  	  
    	QuestionBean bean = new QuestionBean();
    	bean.setId(1);
    	bean.setTitle("テストタイトル");
    	bean.setDescription("テスト説明");
    	bean.setInputNum(2);
    	bean.setPublicFlg(true);
    	
    	questionRepository.save(bean);
    	
    	MvcResult result = mockMvc.perform(post("/teacher/question/edit")
    			.param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/edit"))
            .andReturn();

    	QuestionForm resultForm = (QuestionForm) result.getModelAndView().getModel().get("questionForm");
    	
    	assertEquals(resultForm.getId(), "1");
    	assertEquals(resultForm.getTitle(),"テストタイトル");
    	assertEquals(resultForm.getDescription(),"テスト説明");
    	assertEquals(resultForm.getInputNum(), 2);
    	assertEquals(resultForm.isPublicFlg(), true);
    }
    
    @Test
    public void 先生用問題編集処理() throws Exception {
  	  
    	QuestionBean bean = new QuestionBean();
    	bean.setId(1);
    	bean.setTitle("テストタイトル");
    	bean.setDescription("テスト説明");
    	bean.setInputNum(2);
    	bean.setPublicFlg(true);
    	
    	questionRepository.save(bean);
    	
    	QuestionForm form = new QuestionForm();
    	form.setId("1");
    	form.setTitle("テストタイトル２");
    	form.setDescription("テスト説明２");
    	form.setInputNum(3);
    	form.setPublicFlg(false);
    	
    	MvcResult result = mockMvc.perform(post("/teacher/question/editprocess")
        		.flashAttr("questionForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"))
            .andReturn();

        Optional<QuestionBean> opt = questionRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(resultBean -> {

				assertEquals(resultBean.getId(), 1);
	    		assertEquals(resultBean.getTitle(), "テストタイトル２");
	    		assertEquals(resultBean.getDescription(), "テスト説明２");
	    		assertEquals(resultBean.getInputNum(), 3);
	    		assertEquals(resultBean.isPublicFlg(), false);
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用問題削除処理() throws Exception {
  	  
    	QuestionBean bean = new QuestionBean();
    	bean.setId(1);
    	bean.setTitle("テストタイトル");
    	bean.setDescription("テスト説明");
    	bean.setInputNum(2);
    	bean.setPublicFlg(true);
    	
    	questionRepository.save(bean);
    	
    	QuestionForm form = new QuestionForm();
    	form.setId("1");
    	form.setTitle("テストタイトル２");
    	form.setDescription("テスト説明２");
    	form.setInputNum(3);
    	form.setPublicFlg(false);
    	
    	MvcResult result = mockMvc.perform(post("/teacher/question/delete")
        		.param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"))
            .andReturn();

        long cnt = questionRepository.count();
        assertEquals(cnt, 0);
    }
}
