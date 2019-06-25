package jp.spring.boot.algolearn.controller.teacher;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.ClassForm;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClassControllerTest {

	public static final Operation DELETE_CLASS_ALL
		=Operations.deleteAllFrom("t_class");

	public static final Operation DELETE_USER_ALL
	=Operations.deleteAllFrom("t_user");

	public static final Operation RESET_CLASS_AUTO_INCREMENT
		= Operations.sql("ALTER TABLE t_class ALTER COLUMN id RESTART WITH 1");

	public static final Operation RESET_USER_AUTO_INCREMENT
	= Operations.sql("ALTER TABLE t_user ALTER COLUMN id RESTART WITH 1");

	public static final Operation INSERT_CLASS_DATA1 = 
		Operations.insertInto("t_class")
			.columns("id", "name")
			.values(1, "クラス１")
			.build();

	public static final Operation INSERT_CLASS_DATA2 = 
			Operations.insertInto("t_class")
			.columns("id", "name")
			.values(2, "クラス２")
			.build();

	public static final Operation INSERT_STUDENT_DATA1 = 
			Operations.insertInto("t_user")
				.columns("id", "password", "name", "role_id")
				.values("userid01", "password", "テストユーザー１", RoleCode.ROLE_STUDENT.getString())
				.build();

		public static final Operation INSERT_STUDENT_DATA2 = 
				Operations.insertInto("t_user")
					.columns("id", "password", "name", "role_id")
					.values("userid02", "password", "テストユーザー２", RoleCode.ROLE_STUDENT.getString())
					.build();

		public static final Operation INSERT_USER_CLASS_DATA1 = 
				Operations.insertInto("t_user_class")
					.columns("user_id", "class_id")
					.values("userid01", 1)
					.build();

		public static final Operation INSERT_USER_CLASS_DATA2 = 
				Operations.insertInto("t_user_class")
					.columns("user_id", "class_id")
					.values("userid02", 1)
					.build();

		public static final Operation INSERT_USER_CLASS_DATA3 = 
				Operations.insertInto("t_user_class")
					.columns("user_id", "class_id")
					.values("userid01", 2)
					.build();

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ClassRepository classRepository;
	
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
    public void 先生用クラス一覧ページ表示() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2,
        		INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, 
        		INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/teacher/class"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/class/list"))
            .andReturn();
    	
    	List<ClassForm> list = (List) result.getModelAndView().getModel().get("classes");
    	
    	ClassForm form1 = new ClassForm();
    	form1.setId("1");
    	form1.setName("クラス１");
    	
    	ClassForm form2 = new ClassForm();
    	form2.setId("2");
    	form2.setName("クラス２");
    	
    	assertThat(list, hasItems(form1, form2));
    }
    
    @Test
    public void 先生用クラス登録ページ表示() throws Exception {
  	  
        mockMvc.perform(get("/teacher/class/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/class/add"));
    }
    
    @Test
    public void 先生用クラス登録処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	List<UserBean> list = userRepository.findAll();
    	
    	ClassForm form = new ClassForm();
    	form.setName("クラス１");
    	List<String> userIdList = new ArrayList<>();
    	userIdList.add("userid01");
    	form.setUserCheckedList(userIdList);

    	List<String> userCheckItemList = new ArrayList<>();
    	userCheckItemList.add("userid01");
    	userCheckItemList.add("userid02");
    	
        mockMvc.perform(post("/teacher/class/add")
        		.flashAttr("classForm", form)
        		.flashAttr("userCheckItems", userCheckItemList))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/class"));
        
        Optional<ClassBean> opt = classRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(bean -> {
	    		assertEquals(bean.getName(), form.getName());
	    		for(UserBean userBean : bean.getUserBeans()) {
	    			assertEquals(String.valueOf(userBean.getId()), "userid01");
	    			assertEquals(userBean.getPassword(), "password");
	    			assertEquals(userBean.getName(), "テストユーザー１");
	    		}
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用クラス編集ページ表示() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2, 
        		INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, 
        		INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(post("/teacher/class/edit")
    			.param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/class/edit"))
            .andExpect(model().attributeExists("userCheckItems"))
            .andExpect(model().attribute("userCheckItems", allOf(hasEntry("userid01", "テストユーザー１"), hasEntry("userid02", "テストユーザー２"))))
            .andReturn();

    	ClassForm resultForm = (ClassForm) result.getModelAndView().getModel().get("classForm");
    	
    	assertEquals(resultForm.getId(), "1");
    	assertEquals(resultForm.getName(), "クラス１");
    	assertThat(resultForm.getUserCheckedList(), containsInAnyOrder("userid01", "userid02"));
    }
    
    @Test
    public void 先生用クラス編集処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2, 
        		INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, 
        		INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	ClassForm form = new ClassForm();
    	form.setId("1");
    	form.setName("クラス１－２");
    	List<String> list = new ArrayList<>();
    	list.add("userid02");
    	form.setUserCheckedList(list);

    	List<String> userCheckItemList = new ArrayList<>();
    	userCheckItemList.add("userid01");
    	userCheckItemList.add("userid02");
    	
    	mockMvc.perform(post("/teacher/class/editprocess")
        		.flashAttr("classForm", form)
        		.flashAttr("userCheckItems", userCheckItemList))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/class"));

        Optional<ClassBean> opt = classRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
		opt.ifPresent(bean -> {

	    		assertEquals(bean.getName(), "クラス１－２");
	    		for(UserBean userBean : bean.getUserBeans()) {
	    			assertEquals(String.valueOf(userBean.getId()), "userid02");
	    			assertEquals(userBean.getPassword(), "password");
	    			assertEquals(userBean.getName(), "テストユーザー２");
	    		}
			});
		opt.orElseThrow(()-> new Exception("bean not found."));
    }

    @Test
    public void 先生用クラス削除処理() throws Exception {
  	  
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2, 
        		INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, 
        		INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/teacher/class/delete")
        		.param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/class"));

    	List<ClassBean> list = classRepository.findAll();
        assertEquals(list.size(), 1);
        for(ClassBean bean : list) {
        	assertEquals(bean.getName(), "クラス２");
    		for(UserBean userBean : bean.getUserBeans()) {
    			assertEquals(String.valueOf(userBean.getId()), "userid01");
    			assertEquals(userBean.getPassword(), "password");
    			assertEquals(userBean.getName(), "テストユーザー１");
    		}
        }
    }
}
