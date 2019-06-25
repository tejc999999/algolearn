package jp.spring.boot.algolearn.controller.teacher;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerTest {

	public static final Operation DELETE_ALL
		=Operations.deleteAllFrom("t_question");
	
	public static final Operation RESET_AUTO_INCREMENT
		= Operations.sql("ALTER TABLE t_question ALTER COLUMN id RESTART WITH 1");

	public static final Operation INSERT_DATA1 = 
		Operations.insertInto("t_question")
			.columns("id", "title", "description", "input_num", "public_flg")
			.values(1, "問題タイトル１", "問題説明１", 2, true)
			.build();

	public static final Operation INSERT_DATA2 = 
		Operations.insertInto("t_question")
			.columns("id", "title", "description", "input_num", "public_flg")
			.values(2, "問題タイトル２", "問題説明２", 3, false)
			.build();
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	private DataSource dataSource; 
	
    @Before
    public void テスト前処理() throws Exception {
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
    public void 先生用問題一覧ページ表示() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1, INSERT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/teacher/question"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/list"))
            .andReturn();
    	
    	List<QuestionForm> list = (List) result.getModelAndView().getModel().get("questions");
    	
    	QuestionForm form1 = new QuestionForm();
    	form1.setId("1");
    	form1.setTitle("問題タイトル１");
    	form1.setDescription("問題説明１");
    	form1.setInputNum(2);
    	form1.setPublicFlg(true);
    	
    	QuestionForm form2 = new QuestionForm();
    	form2.setId("2");
    	form2.setTitle("問題タイトル２");
    	form2.setDescription("問題説明２");
    	form2.setInputNum(3);
    	form2.setPublicFlg(false);
    	
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
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(post("/teacher/question/edit")
    			.param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/edit"))
            .andReturn();

    	QuestionForm resultForm = (QuestionForm) result.getModelAndView().getModel().get("questionForm");
    	
    	assertEquals(resultForm.getId(), "1");
    	assertEquals(resultForm.getTitle(),"問題タイトル１");
    	assertEquals(resultForm.getDescription(),"問題説明１");
    	assertEquals(resultForm.getInputNum(), 2);
    	assertEquals(resultForm.isPublicFlg(), true);
    }
    
    @Test
    public void 先生用問題編集処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	QuestionForm form = new QuestionForm();
    	form.setId("1");
    	form.setTitle("問題タイトル１－２");
    	form.setDescription("問題説明１ー２");
    	form.setInputNum(0);
    	form.setPublicFlg(false);
    	
    	mockMvc.perform(post("/teacher/question/editprocess")
        		.flashAttr("questionForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"));

        Optional<QuestionBean> opt = questionRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(resultBean -> {

				assertEquals(resultBean.getId(), 1);
	    		assertEquals(resultBean.getTitle(), "問題タイトル１－２");
	    		assertEquals(resultBean.getDescription(), "問題説明１ー２");
	    		assertEquals(resultBean.getInputNum(), 0);
	    		assertEquals(resultBean.isPublicFlg(), false);
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用問題削除処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/teacher/question/delete")
        		.param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"));

        long cnt = questionRepository.count();
        assertEquals(cnt, 0);
    }
}
