package jp.spring.boot.algolearn.controller.teacher;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.CourseBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.CourseForm;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.CourseRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

/**
 * コースControllerテスト(test class controller)
 * @author tejc999999
 *
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourseControllerTest {

    // テスト用コースデータ作成
    public static final Operation INSERT_COURSE_DATA1 = Operations.insertInto(
            "t_course").columns("id", "name").values(1, "コース１").build();
    public static final Operation INSERT_COURSE_DATA2 = Operations.insertInto(
            "t_course").columns("id", "name").values(2, "コース２").build();

    // テスト用クラスデータ作成
    public static final Operation INSERT_CLASS_DATA1 = Operations.insertInto(
            "t_class").columns("id", "name").values(1, "クラス１").build();
    public static final Operation INSERT_CLASS_DATA2 = Operations.insertInto(
            "t_class").columns("id", "name").values(2, "クラス２").build();

    // テスト用学生データ作成
    public static final Operation INSERT_STUDENT_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "userid01", "password", "テストユーザー１", RoleCode.ROLE_STUDENT
                            .getString()).build();
    public static final Operation INSERT_STUDENT_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "userid02", "password", "テストユーザー２", RoleCode.ROLE_STUDENT
                            .getString()).build();
    public static final Operation INSERT_TEACHER_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "userid03", "password", "テストユーザー３", RoleCode.ROLE_TEACHER
                            .getString()).build();
    public static final Operation INSERT_ADMIN_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "userid04", "password", "テストユーザー４", RoleCode.ROLE_ADMIN
                            .getString()).build();

    // テスト用学生-クラス関連データ作成
    public static final Operation INSERT_USER_CLASS_DATA1 = Operations
            .insertInto("t_user_class").columns("user_id", "class_id").values(
                    "userid01", 1).build();
    // テスト用学生-コース関連データ作成
    public static final Operation INSERT_USER_COURSE_DATA1 = Operations
            .insertInto("t_user_course").columns("user_id", "course_id").values(
                    "userid02", 1).build();
    // テスト用クラス-コース関連データ作成
    public static final Operation INSERT_CLASS_COURSE_DATA1 = Operations
            .insertInto("t_class_course").columns("class_id", "course_id").values(
                    1, 1).build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private DataSource dataSource;

    /**
     * テスト前処理
     * @throws Exception
     */
    @Before
    public void テスト前処理() throws Exception {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
        // mockMvc = MockMvcBuilders.standaloneSetup(new StudentLearnController()).setViewResolvers(viewResolver).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 先生用コース一覧ページ表示_コースあり
     * @throws Exception
     */
    @Test
    public void 先生用コース一覧ページ表示_コースあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_COURSE_DATA1,
                INSERT_COURSE_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(get("/teacher/course"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/list"))
                .andReturn();

        List<CourseForm> list = (List) result.getModelAndView().getModel().get("courses");

        CourseForm form1 = new CourseForm();
        form1.setId("1");
        form1.setName("コース１");

        CourseForm form2 = new CourseForm();
        form2.setId("2");
        form2.setName("コース２");

        assertThat(list, hasItems(form1, form2));
    }

    /**
     * 先生用コース一覧ページ表示_コースなし
     * @throws Exception
     */
    @Test
    public void 先生用コース一覧ページ表示_コースなし() throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/course")).andExpect(
                status().isOk()).andExpect(view().name("teacher/course/list"))
                .andReturn();

        List<CourseForm> list = (List) result.getModelAndView().getModel().get("courses");
        if (list != null)
            assertEquals(list.size(), 0);
    }
    
    /**
     * 先生用コース登録ページ表示_クラスあり_ユーザーあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録ページ表示_クラスあり_ユーザーあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(get("/teacher/course/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems",
                        allOf(hasEntry("1", "クラス１"),
                                hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        allOf(hasEntry("userid01", "テストユーザー１"),
                                hasEntry("userid02","テストユーザー２"))))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();
        
        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
    }

    /**
     * 先生用コース登録ページ表示_クラスなし_ユーザーなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録ページ表示_クラスなし_ユーザーなし() throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/course/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();
        
        Map<String, String> classMap = (Map<String, String>) result.getModelAndView().getModel().get("classCheckItems");
        if(classMap != null) assertEquals(classMap.size(), 0);

        Map<String, String> userMap = (Map<String, String>) result.getModelAndView().getModel().get("userCheckItems");
        if(userMap != null) assertEquals(userMap.size(), 0);
    }
    
    /**
     * 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2,  INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm sendCourseForm = new CourseForm();
        List<String> sendClassCheckedList = new ArrayList<String>();
        sendClassCheckedList.add("1");
        sendCourseForm.setClassCheckedList(sendClassCheckedList);
        MvcResult result = mockMvc.perform(post("/teacher/course/add")
                    .param("userExclusionBtn", "クラスユーザ除外")
                    .flashAttr("courseForm", sendCourseForm))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems",
                        allOf(hasEntry("1", "クラス１"),
                                hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        hasEntry("userid02","テストユーザー２")))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();

        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 1);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel().get("courseForm");
        List<String> classCheckedList = courseForm.getClassCheckedList();
        assertEquals(classCheckedList.size(), 1);
        assertThat(classCheckedList, containsInAnyOrder("1"));
        
        List<String> userCheckedList = courseForm.getUserCheckedList();
        if(userCheckedList != null) assertEquals(userCheckedList.size(), 0);
    }
    
    /**
     * 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm sendCourseForm = new CourseForm();
        List<String> sendClassCheckedList = new ArrayList<String>();
        sendClassCheckedList.add("2");
        sendCourseForm.setClassCheckedList(sendClassCheckedList);
        MvcResult result = mockMvc.perform(get("/teacher/course/add")
                    .param("userExclusionBtn", "クラスユーザ除外")
                    .flashAttr("courseForm", sendCourseForm))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems", 
                        allOf(hasEntry("1", "クラス１"),
                                hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        allOf(hasEntry("userid01", "テストユーザー１"),
                                hasEntry("userid02","テストユーザー２"))))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();

        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);

        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel().get("courseForm");

        List<String> classCheckedList = courseForm.getClassCheckedList();
        assertEquals(classCheckedList.size(), 1);
        assertThat(classCheckedList, containsInAnyOrder("2"));
        
        List<String> userCheckedList = courseForm.getUserCheckedList();
        if(userCheckedList != null) assertEquals(userCheckedList.size(), 0);
    }

    /**
     * 先生用コース登録処理_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> classList = new ArrayList<>();
        classList.add("1");
        form.setClassCheckedList(classList);
        List<String> userIdList = new ArrayList<>();
        userIdList.add("userid02");
        form.setUserCheckedList(userIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> optCourse = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        optCourse.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(classBean.getId(), 1);
                    assertEquals(classBean.getName(), "クラス１");
                    Set<UserBean> userBeanSet = classBean.getUserBeans();
                    userBeanSet.forEach(userBean -> {
                        assertEquals(String.valueOf(userBean.getId()), "userid01");
                        assertEquals(userBean.getPassword(), "password");
                        assertEquals(userBean.getName(), "テストユーザー１");
                    });
            });
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            assertEquals(userBeanSet.size(), 1);
            if (userBeanSet != null) userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid02");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー２");
            });
        });
    }
    
    /**
     * 先生用コース登録処理_ユーザーあり_クラスあり_ユーザ重複
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスあり_ユーザ重複() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> classList = new ArrayList<>();
        classList.add("1");
        form.setClassCheckedList(classList);
        List<String> userIdList = new ArrayList<>();
        userIdList.add("userid01");
        userIdList.add("userid02");
        form.setUserCheckedList(userIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> optCourse = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        optCourse.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(classBean.getId(), 1);
                    assertEquals(classBean.getName(), "クラス１");
                    Set<UserBean> userBeanSet = classBean.getUserBeans();
                    userBeanSet.forEach(userBean -> {
                        assertEquals(String.valueOf(userBean.getId()), "userid01");
                        assertEquals(userBean.getPassword(), "password");
                        assertEquals(userBean.getName(), "テストユーザー１");
                    });
            });
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            assertEquals(userBeanSet.size(), 1);
            if (userBeanSet != null) userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid02");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー２");
            });
        });
    }
    
    /**
     * 先生用コース登録処理_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> userIdList = new ArrayList<>();
        userIdList.add("userid01");
        form.setUserCheckedList(userIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> optCourse = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        optCourse.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 0);
            if (classBeanSet != null) assertEquals(classBeanSet.size(), 0);
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            assertEquals(userBeanSet.size(), 1);
            if (userBeanSet != null) userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid01");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー１");
            });
        });
    }
    
    /**
     * 先生用コース登録処理_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーなし_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> classList = new ArrayList<>();
        classList.add("1");
        form.setClassCheckedList(classList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> optCourse = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        optCourse.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(classBean.getId(), 1);
                    assertEquals(classBean.getName(), "クラス１");
                    Set<UserBean> userBeanSet = classBean.getUserBeans();
                    userBeanSet.forEach(userBean -> {
                        assertEquals(String.valueOf(userBean.getId()), "userid01");
                        assertEquals(userBean.getPassword(), "password");
                        assertEquals(userBean.getName(), "テストユーザー１");
                    });
            });
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            if(userBeanSet != null) assertEquals(userBeanSet.size(), 0);
        });
    }
    
    /**
     * 先生用コース登録処理_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーなし_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm form = new CourseForm();
        form.setName("コース１");

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> optCourse = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        optCourse.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            if (classBeanSet != null) assertEquals(classBeanSet.size(), 0);
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            if (userBeanSet != null) assertEquals(userBeanSet.size(), 0);
        });
    }

    /**
     * 先生用コース編集ページ表示_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ表示_ユーザーあり_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(post("/teacher/course/edit")
                    .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/edit"))
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems", 
                        allOf(hasEntry("1", "クラス１"),
                                hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        allOf(hasEntry("userid01", "テストユーザー１"),
                                hasEntry("userid02","テストユーザー２"))))
                .andReturn();

        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        assertEquals(courseForm.getClassCheckedList().size(), 1);
        assertThat(courseForm.getClassCheckedList(), containsInAnyOrder("1"));
        assertEquals(courseForm.getUserCheckedList().size(), 1);
        assertThat(courseForm.getUserCheckedList(), containsInAnyOrder("userid02"));
    }
    
    /**
     * 先生用コース編集ページ表示_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ表示_ユーザーなし_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_COURSE_DATA1, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(post("/teacher/course/edit")
                    .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/edit"))
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems",
                        allOf(hasEntry("1", "クラス１"),
                               hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        allOf(hasEntry("userid01", "テストユーザー１"),
                               hasEntry("userid02","テストユーザー２"))))
                .andReturn();

        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        List<String> classIdList = courseForm.getClassCheckedList();
        if(classIdList != null) assertEquals(classIdList.size(), 0);
        List<String> userIdList = courseForm.getUserCheckedList();
        if(userIdList != null) assertEquals(userIdList.size(), 0);
    }

    
    /**
     * 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm sendCourseForm = new CourseForm();
        sendCourseForm.setId("1");
        sendCourseForm.setName("コース１");
        List<String> sendClassCheckedList = new ArrayList<String>();
        sendClassCheckedList.add("1");
        sendCourseForm.setClassCheckedList(sendClassCheckedList);
        MvcResult result = mockMvc.perform(post("/teacher/course/editprocess")
                    .param("userExclusionBtn", "クラスユーザ除外")
                    .flashAttr("courseForm", sendCourseForm))
                .andExpect(model().attributeExists("classCheckItems"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("classCheckItems",
                        allOf(hasEntry("1", "クラス１"),
                               hasEntry("2","クラス２"))))
                .andExpect(model().attribute("userCheckItems",
                        hasEntry("userid02","テストユーザー２")))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/edit"))
                .andReturn();
        
        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 1);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        List<String> classIdList = courseForm.getClassCheckedList();
        assertEquals(classIdList.size(), 1);
        classIdList.forEach(classId -> {
            Optional<ClassBean> optClass = classRepository.findById(Integer.parseInt(classId));
            optClass.ifPresent(classBean -> {
                assertEquals(classBean.getId(), 1);
                assertEquals(classBean.getName(), "クラス１");
                Set<UserBean> userBeanSet = classBean.getUserBeans();
                userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid01");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー１");
                });            });
        });
        List<String> userIdList = courseForm.getClassCheckedList();
        assertEquals(userIdList.size(), 1);
        userIdList.forEach(userId -> {
            Optional<UserBean> optUser = userRepository.findById(userId);
            optUser.ifPresent(userBean -> {
                assertEquals(userBean.getId(), "userid02");
                assertEquals(userBean.getPassword(), "password");
                assertEquals(userBean.getName(), "テストユーザー２");
            });
        });
    }
    /**
     * 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
        CourseForm sendCourseForm = new CourseForm();
        sendCourseForm.setId("1");
        sendCourseForm.setName("コース１");
        List<String> sendClassCheckedList = new ArrayList<String>();
        sendClassCheckedList.add("2");
        sendCourseForm.setClassCheckedList(sendClassCheckedList);
        MvcResult result = mockMvc.perform(post("/teacher/course/editprocess")
                .param("userExclusionBtn", "クラスユーザ除外")
                .flashAttr("courseForm", sendCourseForm))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/edit"))
                .andReturn();

        // まだMavenのHamcrestでCollection系のサイズチェックができないため、ここでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        List<String> classIdList = courseForm.getClassCheckedList();
        assertEquals(classIdList.size(), 1);
        classIdList.forEach(classId -> {
            Optional<ClassBean> optClass = classRepository.findById(Integer.parseInt(classId));
            optClass.ifPresent(classBean -> {
                assertEquals(classBean.getId(), 2);
                assertEquals(classBean.getName(), "クラス２");
                Set<UserBean> userBeanSet = classBean.getUserBeans();
                if(userBeanSet != null) assertEquals(userBeanSet.size(), 0);
            });
        });
        List<String> userIdList = courseForm.getUserCheckedList();
        if(userIdList != null) assertEquals(userIdList.size(), 0);
    }

    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーあり_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> list = new ArrayList<>();
        list.add("userid02");
        courseForm.setUserCheckedList(list);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            assertEquals(userBeanSet.size(), 1);
            if (userBeanSet != null) userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid02");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー２");
                });
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            if(classBeanSet != null) assertEquals(classBeanSet.size(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> classIdList = new ArrayList<>();
        classIdList.add("1");
        courseForm.setClassCheckedList(classIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            if(userBeanSet != null) assertEquals(userBeanSet.size(), 0);
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(String.valueOf(classBean.getId()), "1");
                    assertEquals(classBean.getName(), "クラス１");
                });
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA1,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            if(userBeanSet != null) assertEquals(userBeanSet.size(), 0);
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            if (classBeanSet != null) assertEquals(classBeanSet.size(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスなし_変更後_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスなし_変更後_ユーザーなし_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> classIdList = new ArrayList<>();
        classIdList.add("1");
        courseForm.setClassCheckedList(classIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            if(userBeanSet != null) assertEquals(userBeanSet.size(), 0);
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(String.valueOf(classBean.getId()), "1");
                    assertEquals(classBean.getName(), "クラス１");
                    Set<UserBean> classUserBeanSet = classBean.getUserBeans();
                    assertEquals(classUserBeanSet.size(), 1);
                    classUserBeanSet.forEach(userBean -> {
                        assertEquals(userBean.getId(), "userid01");
                        assertEquals(userBean.getPassword(), "password");
                        assertEquals(userBean.getName(), "テストユーザー１");
                    });
                });
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用コース編集処理_ユーザーなし_クラスなし_変更後_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーなし_クラスなし_変更後_ユーザーあり_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_COURSE_DATA1, INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> classIdList = new ArrayList<>();
        classIdList.add("1");
        courseForm.setClassCheckedList(classIdList);
        List<String> userIdList = new ArrayList<>();
        userIdList.add("userid02");
        courseForm.setUserCheckedList(userIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(1);
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            assertEquals(userBeanSet.size(), 1);
            if (userBeanSet != null) userBeanSet.forEach(userBean -> {
                    assertEquals(String.valueOf(userBean.getId()), "userid02");
                    assertEquals(userBean.getPassword(), "password");
                    assertEquals(userBean.getName(), "テストユーザー２");
                });
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            assertEquals(classBeanSet.size(), 1);
            if (classBeanSet != null) classBeanSet.forEach(classBean -> {
                    assertEquals(String.valueOf(classBean.getId()), "1");
                    assertEquals(classBean.getName(), "クラス１");
                    Set<UserBean> classUserBeanSet = classBean.getUserBeans();
                    assertEquals(classUserBeanSet.size(), 1);
                    classUserBeanSet.forEach(userBean -> {
                        assertEquals(userBean.getId(), "userid01");
                        assertEquals(userBean.getPassword(), "password");
                        assertEquals(userBean.getName(), "テストユーザー１");
                    });
                });
        });
    }
    
 
    /**
     * 先生用コース削除処理_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーあり_クラスあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1, 
                INSERT_USER_COURSE_DATA1, INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> userOpt1 = userRepository.findById("userid01");
        userOpt1.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });

        Optional<UserBean> userOpt2 = userRepository.findById("userid02");
        userOpt2.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
        
        Optional<ClassBean> classOpt = classRepository.findById(1);
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), 1);
            assertEquals(classBean.getName(), "クラス１");
            Set<UserBean> userBeanSet = classBean.getUserBeans();
            userBeanSet.forEach(userBean -> {
                assertEquals(userBean.getId(), "userid01");
                assertEquals(userBean.getPassword(), "password");
                assertEquals(userBean.getName(), "テストユーザー１");
            });
            Set<CourseBean> courseBeanSet = classBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
    }

    /**
     * 先生用コース削除処理_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーあり_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1, 
                INSERT_USER_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> userOpt2 = userRepository.findById("userid02");
        userOpt2.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
    }
    
    /**
     * 先生用コース削除処理_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーなし_クラスあり() throws Exception {


        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1, 
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> userOpt1 = userRepository.findById("userid01");
        userOpt1.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
        
        Optional<ClassBean> classOpt = classRepository.findById(1);
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), 1);
            assertEquals(classBean.getName(), "クラス１");
            Set<UserBean> userBeanSet = classBean.getUserBeans();
            userBeanSet.forEach(userBean -> {
                assertEquals(userBean.getId(), "userid01");
                assertEquals(userBean.getPassword(), "password");
                assertEquals(userBean.getName(), "テストユーザー１");
            });
            Set<CourseBean> courseBeanSet = classBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
    }
    
    
    /**
     * 先生用コース削除処理_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーなし_クラスなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> userOpt1 = userRepository.findById("userid01");
        userOpt1.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });

        Optional<UserBean> userOpt2 = userRepository.findById("userid02");
        userOpt2.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "userid02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            Set<CourseBean> courseBeanSet = userBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
        
        Optional<ClassBean> classOpt = classRepository.findById(1);
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), 1);
            assertEquals(classBean.getName(), "クラス１");
            Set<UserBean> userBeanSet = classBean.getUserBeans();
            userBeanSet.forEach(userBean -> {
                assertEquals(userBean.getId(), "userid01");
                assertEquals(userBean.getPassword(), "password");
                assertEquals(userBean.getName(), "テストユーザー１");
            });
            Set<CourseBean> courseBeanSet = classBean.getCourseBeans();
            if(courseBeanSet != null) assertEquals(courseBeanSet.size(), 0);
        });
    }
}
