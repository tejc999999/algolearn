package jp.spring.boot.algolearn.controller.teacher;

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
import javax.sql.DataSource;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
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

import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.StudentForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentControllerTest {

	public static final Operation DELETE_ALL
		=Operations.deleteAllFrom("t_user");

	public static final Operation INSERT_DATA1 = 
		Operations.insertInto("t_user")
			.columns("id", "password", "name", "role_id")
			.values("userid01", "password", "テストユーザー１", RoleCode.ROLE_STUDENT.getString())
			.build();

	public static final Operation INSERT_DATA2 = 
			Operations.insertInto("t_user")
				.columns("id", "password", "name", "role_id")
				.values("userid02", "password", "テストユーザー２", RoleCode.ROLE_STUDENT.getString())
				.build();
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
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
    public void 先生用学生一覧ページ表示() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1, INSERT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/teacher/student"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/student/list"))
            .andReturn();
    	
    	List<StudentForm> list = (List) result.getModelAndView().getModel().get("students");
    	
    	StudentForm form1 = new StudentForm();
    	form1.setId("userid01");
    	form1.setPassword("password");
    	form1.setName("テストユーザー１");
    	
    	StudentForm form2 = new StudentForm();
    	form2.setId("userid02");
    	form2.setPassword("password");
    	form2.setName("テストユーザー２");
    	
    	assertThat(list, hasItems(form1, form2));
    }
    
    @Test
    public void 先生用学生登録ページ表示() throws Exception {
  	  
        mockMvc.perform(get("/teacher/student/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/student/add"));
    }
    
    @Test
    public void 先生用学生登録処理() throws Exception {
  	  
    	StudentForm form = new StudentForm();
    	form.setId("userid01");
    	form.setPassword("password");
    	form.setName("テストユーザー１");
    	
        mockMvc.perform(post("/teacher/student/add")
        		.flashAttr("studentForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/student"));
        
        Optional<UserBean> opt = userRepository.findById("userid01");
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(bean -> {

	    		assertEquals(bean.getId(), form.getId());
	    		assertEquals(bean.getPassword(),form.getPassword());
	    		assertEquals(bean.getName(), form.getName());
	    		assertEquals(bean.getRoleId(), RoleCode.ROLE_STUDENT.getString());
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用学生編集ページ表示() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(post("/teacher/student/edit")
    			.param("id", "userid01"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/student/edit"))
            .andReturn();

    	StudentForm resultForm = (StudentForm) result.getModelAndView().getModel().get("studentForm");
    	
    	assertEquals(resultForm.getId(), "userid01");
    	assertEquals(resultForm.getPassword(),"password");
    	assertEquals(resultForm.getName(),"テストユーザー１");
    	assertEquals(resultForm.getRoleId(), RoleCode.ROLE_STUDENT.getString());
    }
    
    @Test
    public void 先生用学生編集処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	StudentForm form = new StudentForm();
    	form.setId("userid01");
    	form.setName("テストユーザー１－２");
    	form.setPassword("password2");
    	
    	mockMvc.perform(post("/teacher/student/editprocess")
        		.flashAttr("studentForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/student"));

        Optional<UserBean> opt = userRepository.findById("userid01");
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(resultBean -> {

				assertEquals(resultBean.getId(), "userid01");
	    		assertEquals(resultBean.getPassword(), "password2");
	    		assertEquals(resultBean.getName(), "テストユーザー１－２");
	    		assertEquals(resultBean.getRoleId(), RoleCode.ROLE_STUDENT.getString());
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用学生削除処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/teacher/student/delete")
        		.param("id", "userid01"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/student"));

        long cnt = userRepository.count();
        assertEquals(cnt, 0);
    }
}
