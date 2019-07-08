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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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

    // テスト用ユーザー（学生、先生、管理者）データ作成
    public static final Operation INSERT_STUDENT_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user01", "password", "テストユーザー１", RoleCode.ROLE_STUDENT
                            .getString()).build();
    public static final Operation INSERT_STUDENT_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user02", "password", "テストユーザー２", RoleCode.ROLE_STUDENT
                            .getString()).build();
    public static final Operation INSERT_TEACHER_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user03", "password", "テストユーザー３", RoleCode.ROLE_TEACHER
                            .getString()).build();
    public static final Operation INSERT_ADMIN_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user04", "password", "テストユーザー４", RoleCode.ROLE_ADMIN
                            .getString()).build();

    // テスト用学生-クラス関連データ作成
    public static final Operation INSERT_USER_CLASS_DATA1 = Operations
            .insertInto("t_user_class").columns("user_id", "class_id").values(
                    "user01", 1).build();
    
    // テスト用学生-コース関連データ作成
    public static final Operation INSERT_USER_COURSE_DATA1 = Operations
            .insertInto("t_user_course").columns("user_id", "course_id").values(
                    "user01", 1).build();
    public static final Operation INSERT_USER_COURSE_DATA2 = Operations
            .insertInto("t_user_course").columns("user_id", "course_id").values(
                    "user02", 1).build();
    
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

        // DB状態
        // コース：コース１（学生なし、クラスなし）、コース２（学生なし、クラスなし）
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

        // DB状態
        // コース：コース１（学生なし、クラスなし）、コース２（学生なし、クラスなし）
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), (先生), user04(管理者)
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
                        allOf(hasEntry("user01", "テストユーザー１"),
                                hasEntry("user02","テストユーザー２"))))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();
        
        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
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

        // DB状態
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2,  INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス１(user01)を選択
        // ・クラス所属ユーザを除外するボタンを押す
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
                        hasEntry("user02","テストユーザー２")))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 1);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel().get("courseForm");
        assertEquals(courseForm.getClassCheckedList().size(), 1);
        assertEquals(courseForm.getClassCheckedList().get(0), "1");
        assertEquals(courseForm.getUserCheckedList(), null);
    }
    
    /**
     * 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録ページ内クラス所属ユーザ除外_所属ユーザなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス２(学生なし)を選択
        // ・クラス所属ユーザを除外するボタンを押す
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
                        allOf(hasEntry("user01", "テストユーザー１"),
                                hasEntry("user02","テストユーザー２"))))
                .andExpect(view().name("teacher/course/add"))
                .andReturn();

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);

        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel().get("courseForm");

        assertEquals(courseForm.getClassCheckedList().size(), 1);
        assertThat(courseForm.getClassCheckedList(), containsInAnyOrder("2"));
        assertEquals(courseForm.getUserCheckedList(), null);
    }

    /**
     * 先生用コース登録処理_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・学生（user02）を選択する
        // ・クラス（クラス２（学生なし））を選択する
        // ・コース名をコース１とする
        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        form.setClassCheckedList(checkClassIdList);
        List<String> checkUserIdList = new ArrayList<>();
        checkUserIdList.add("user02");
        form.setUserCheckedList(checkUserIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１");
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
            assertEquals(courseBean.getUserIdList().size(), 1);
            assertEquals(courseBean.getUserIdList().get(0), "user02");
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース登録処理_ユーザーあり_クラスあり_ユーザ重複
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスあり_ユーザ重複() throws Exception {

        // DB状態
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・学生（user01, user02）を選択する
        // ・クラス(user01)を選択する
        // ・コース名をコース１とする
        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        form.setClassCheckedList(checkClassIdList);
        List<String> checkUserIdList = new ArrayList<>();
        checkUserIdList.add("user01");
        checkUserIdList.add("user02");
        form.setUserCheckedList(checkUserIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
            assertEquals(courseBean.getUserIdList().size(), 2);
            assertThat(courseBean.getUserIdList(), hasItems("user01", "user02"));
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    
    /**
     * 先生用コース登録処理_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーあり_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01)、クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・学生（user01, user02）を選択する
        // ・クラスを選択しない
        // ・コース名をコース１とする
        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> checkUserIdList = new ArrayList<>();
        checkUserIdList.add("user01");
        form.setUserCheckedList(checkUserIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            assertEquals(courseBean.getClassIdList().size(), 0);
            assertEquals(courseBean.getUserIdList().size(), 1);
            assertEquals(courseBean.getUserIdList().get(0), "user01");
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース登録処理_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーなし_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01)
        // ユーザー：user01(学生), user02(学生)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス（user01）を選択する
        // ・学生を選択しない
        // ・コース名をコース１とする
        CourseForm form = new CourseForm();
        form.setName("コース１");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        form.setClassCheckedList(checkClassIdList);

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
            assertEquals(courseBean.getUserIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース登録処理_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース登録処理_ユーザーなし_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01)
        // ユーザー：user01(学生), user02(学生)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラスを選択しない
        // ・学生を選択しない
        // ・コース名をコース１とする
        CourseForm form = new CourseForm();
        form.setName("コース１");

        mockMvc.perform(post("/teacher/course/add").flashAttr("courseForm", form))
                .andExpect(status().is3xxRedirection()).andExpect(view().name(
                        "redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), form.getName());
            assertEquals(courseBean.getClassIdList().size(), 0);
            assertEquals(courseBean.getUserIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));

    }

    /**
     * 先生用コース編集ページ表示_ユーザーあり_クラスあり_ユーザー重複なし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ表示_ユーザーあり_クラスあり_ユーザー重複なし() throws Exception {

        // DB状態
        // クラス：クラス１（user01)
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        // コース：コース１（学生(user02), クラス（クラス１（学生（user01)))
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA2,
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
                        allOf(hasEntry("user01", "テストユーザー１"),
                                hasEntry("user02","テストユーザー２"))))
                .andReturn();

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
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
        assertThat(courseForm.getUserCheckedList(), containsInAnyOrder("user02"));
    }
    
    /**
     * 先生用コース編集ページ表示_ユーザーあり_クラスあり_ユーザー重複あり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ表示_ユーザーあり_クラスあり_ユーザー重複あり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        // コース：コース１（学生(user02), クラス（クラス１（学生（user01)))
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA1, INSERT_USER_COURSE_DATA2,
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
                        allOf(hasEntry("user01", "テストユーザー１"),
                                hasEntry("user02","テストユーザー２"))))
                .andReturn();

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
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
        assertEquals(courseForm.getUserCheckedList().size(), 2);
        assertThat(courseForm.getUserCheckedList(), containsInAnyOrder("user01", "user02"));
    }
    
    /**
     * 先生用コース編集ページ表示_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ表示_ユーザーなし_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        // コース：コース１（学生なし、クラスなし）
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
                        allOf(hasEntry("user01", "テストユーザー１"),
                               hasEntry("user02","テストユーザー２"))))
                .andReturn();

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        assertEquals(courseForm.getClassCheckedList().size(), 0);
        assertEquals(courseForm.getUserCheckedList().size(), 0);
    }

    
    /**
     * 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA2,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス１(user01)を選択する
        // ・クラス所属ユーザを除外する
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
                        hasEntry("user02","テストユーザー２")))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/course/edit"))
                .andReturn();
        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 1);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        assertEquals(courseForm.getClassCheckedList().size(), 1);
        assertEquals(courseForm.getClassCheckedList().get(0), "1");
        assertEquals(courseForm.getUserCheckedList(), null);
    }
    /**
     * 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集ページ内クラス所属ユーザ除外_所属ユーザなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生), user03(先生), user04(管理者)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA1, INSERT_ADMIN_DATA1,
                INSERT_CLASS_DATA1, INSERT_CLASS_DATA2, INSERT_USER_CLASS_DATA1,
                INSERT_COURSE_DATA1, INSERT_USER_COURSE_DATA2,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
        // ・クラス２(学生なし)を選択する
        // ・クラス所属ユーザを除外する
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

        // Maven対応のHamcrestバージョンではCollection系のサイズチェックができないため、andExpectではチェックできない
        // Mvcresultでチェック
        Map<String, String> classCheckMap = (Map) result.getModelAndView().getModel().get("classCheckItems");
        assertEquals(classCheckMap.size(), 2);
        Map<String, String> userCheckMap = (Map) result.getModelAndView().getModel().get("userCheckItems");
        assertEquals(userCheckMap.size(), 2);
        
        CourseForm courseForm = (CourseForm) result.getModelAndView().getModel()
                .get("courseForm");

        assertEquals(courseForm.getId(), "1");
        assertEquals(courseForm.getName(), "コース１");
        assertEquals(courseForm.getClassCheckedList().size(), 1);
        assertEquals(courseForm.getClassCheckedList().get(0), "2");
        assertEquals(courseForm.getUserCheckedList(), null);
    }

    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーあり_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA2,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・学生（user02）を選択する
        // ・クラスを選択しない
        // ・コース名をコース１－２にする
        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> list = new ArrayList<>();
        list.add("user02");
        courseForm.setUserCheckedList(list);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            assertEquals(courseBean.getUserIdList().size(), 1);
            assertEquals(courseBean.getUserIdList().get(0), "user02");
            assertEquals(courseBean.getClassIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA2,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス１（user01）を選択する
        // ・学生を選択しない
        // ・コース名をコース１－２とする
        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        courseForm.setClassCheckedList(checkClassIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            assertEquals(courseBean.getUserIdList().size(), 0);
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスあり_変更後_ユーザーなし_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA2,
                INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラスを選択しない
        // ・学生を選択しない
        // ・コース名をコース１－２に変更する
        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            assertEquals(courseBean.getUserIdList().size(), 0);
            assertEquals(courseBean.getClassIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用コース編集処理_ユーザーあり_クラスなし_変更後_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーあり_クラスなし_変更後_ユーザーなし_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラスなし）
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_CLASS_DATA2, INSERT_COURSE_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_COURSE_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス（クラス１（user01））を選択する
        // ・学生を選択しない
        //　・コース名をコース１－２に変更する
        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        courseForm.setClassCheckedList(checkClassIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            assertEquals(courseBean.getUserIdList().size(), 0);
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用コース編集処理_ユーザーなし_クラスなし_変更後_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース編集処理_ユーザーなし_クラスなし_変更後_ユーザーあり_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生なし、クラスなし）
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_COURSE_DATA1, INSERT_USER_CLASS_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス（クラス１（user01））を選択する
        // ・学生（user02）を選択する
        // ・コース名をコース１－２に変更する
        CourseForm courseForm = new CourseForm();
        courseForm.setId("1");
        courseForm.setName("コース１－２");
        List<String> checkClassIdList = new ArrayList<>();
        checkClassIdList.add("1");
        courseForm.setClassCheckedList(checkClassIdList);
        List<String> checkUserIdList = new ArrayList<>();
        checkUserIdList.add("user02");
        courseForm.setUserCheckedList(checkUserIdList);

        mockMvc.perform(post("/teacher/course/editprocess").
                flashAttr("courseForm", courseForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/course"));

        Optional<CourseBean> opt = courseRepository.findById(new Long(1));
        opt.ifPresent(courseBean -> {
            assertEquals(courseBean.getName(), "コース１－２");
            assertEquals(courseBean.getUserIdList().size(), 1);
            assertEquals(courseBean.getUserIdList().get(0), "user02");
            assertEquals(courseBean.getClassIdList().size(), 1);
            assertEquals(courseBean.getClassIdList().get(0), "1");
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));

    }
    
 
    /**
     * 先生用コース削除処理_ユーザーあり_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーあり_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラス（クラス１（user01）））
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1, 
                INSERT_USER_COURSE_DATA2, INSERT_CLASS_COURSE_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> userOpt1 = userRepository.findById("user01");
        userOpt1.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 1);
            assertEquals(userBean.getClassIdList().get(0), "1");
        });
        userOpt1.orElseThrow(() -> new Exception("bean not found."));

        Optional<UserBean> userOpt2 = userRepository.findById("user02");
        userOpt2.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 0);
        });
        userOpt2.orElseThrow(() -> new Exception("bean not found."));

        
        Optional<ClassBean> classOpt = classRepository.findById(new Long(1));
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), new Long(1));
            assertEquals(classBean.getName(), "クラス１");
            assertEquals(classBean.getUserIdList().size(), 1);
            assertEquals(classBean.getUserIdList().get(0), "user01");
            assertEquals(classBean.getCourseIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        classOpt.orElseThrow(() -> new Exception("bean not found."));

    }

    /**
     * 先生用コース削除処理_ユーザーあり_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーあり_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生（user02）、クラスなし）
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_COURSE_DATA1, 
                INSERT_USER_COURSE_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/course/delete")
                    .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/course"));

        List<CourseBean> courseList = courseRepository.findAll();
        if(courseList != null) assertEquals(courseList.size(), 0);
        
        Optional<UserBean> opt = userRepository.findById("user02");
        opt.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));

    }
    
    /**
     * 先生用コース削除処理_ユーザーなし_クラスあり
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーなし_クラスあり() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生なし、クラス(クラス１(user01))）
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
        
        Optional<UserBean> userOpt = userRepository.findById("user01");
        userOpt.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 1);
            assertEquals(userBean.getClassIdList().get(0), "1");
        });
        userOpt.orElseThrow(() -> new Exception("bean not found."));

        
        Optional<ClassBean> classOpt = classRepository.findById(new Long(1));
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), new Long(1));
            assertEquals(classBean.getName(), "クラス１");
            assertEquals(classBean.getUserIdList().size(), 1);
            assertEquals(classBean.getUserIdList().get(0), "user01");
            assertEquals(classBean.getCourseIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        classOpt.orElseThrow(() -> new Exception("bean not found."));

    }
    
    
    /**
     * 先生用コース削除処理_ユーザーなし_クラスなし
     * @throws Exception
     */
    @Test
    public void 先生用コース削除処理_ユーザーなし_クラスなし() throws Exception {

        // DB状態
        // クラス：クラス１（user01), クラス２（学生なし）
        // ユーザー：user01(学生), user02(学生)
        // コース：コース１（学生なし、クラスなし）
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
        
        Optional<UserBean> userOpt1 = userRepository.findById("user01");
        userOpt1.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user01");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー１");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 1);
            assertEquals(userBean.getClassIdList().get(0), "1");
        });
        userOpt1.orElseThrow(() -> new Exception("bean not found."));

        Optional<UserBean> userOpt2 = userRepository.findById("user02");
        userOpt2.ifPresent(userBean -> {
            assertEquals(userBean.getId(), "user02");
            assertEquals(userBean.getPassword(), "password");
            assertEquals(userBean.getName(), "テストユーザー２");
            assertEquals(userBean.getCourseIdList().size(), 0);
            assertEquals(userBean.getClassIdList().size(), 0);
        });
        // ifPresentOrElseの実装はJDK9からの様子
        userOpt2.orElseThrow(() -> new Exception("bean not found."));

        
        Optional<ClassBean> classOpt = classRepository.findById(new Long(1));
        classOpt.ifPresent(classBean -> {
            assertEquals(classBean.getId(), new Long(1));
            assertEquals(classBean.getName(), "クラス１");
            assertEquals(classBean.getUserIdList().size(), 1);
            assertEquals(classBean.getUserIdList().get(0), "user01");
            assertEquals(classBean.getCourseIdList().size(), 0);
        });
    }
}
