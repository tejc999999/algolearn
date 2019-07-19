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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.teacher.form.QuestionForm;

/**
 * 問題Controllerテスト(question class controller)
 * @author tejc999999
 *
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerTest {

    // テスト用問題データ作成
    public static final Operation INSERT_DATA1 = Operations.insertInto(
            "t_question").columns("id", "title", "description", "input_num")
            .values(1, "問題タイトル１", "問題説明１", 2).build();

    public static final Operation INSERT_DATA2 = Operations.insertInto(
            "t_question").columns("id", "title", "description", "input_num")
            .values(2, "問題タイトル２", "問題説明２", 3).build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    QuestionRepository questionRepository;

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
     * 先生用問題一覧ページ表示_問題あり
     * @throws Exception
     */
    @Test
    public void 先生用問題一覧ページ表示_問題あり() throws Exception {

        // DB状態
        // 問題：問題１、問題２
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1, INSERT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(get("/teacher/question")).andExpect(
                status().isOk()).andExpect(view().name("teacher/question/list"))
                .andReturn();

        List<QuestionForm> list = (List) result.getModelAndView().getModel().get("questions");

        QuestionForm form1 = new QuestionForm();
        form1.setId("1");
        form1.setTitle("問題タイトル１");
        form1.setDescription("問題説明１");
        form1.setInputNum(2);

        QuestionForm form2 = new QuestionForm();
        form2.setId("2");
        form2.setTitle("問題タイトル２");
        form2.setDescription("問題説明２");
        form2.setInputNum(3);

        assertThat(list, hasItems(form1, form2));
    }

    /**
     * 先生用問題一覧ページ表示_問題なし
     * @throws Exception
     */
    @Test
    public void 先生用問題一覧ページ表示_問題なし() throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/question"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/question/list"))
                .andReturn();

        List<QuestionForm> list = (List) result.getModelAndView().getModel().get("questions");
        if (list != null)
            assertEquals(list.size(), 0);
    }

    /**
     * 先生用問題登録ページ表示
     * @throws Exception
     */
    @Test
    public void 先生用問題登録ページ表示() throws Exception {

        mockMvc.perform(get("/teacher/question/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/question/add"));
    }

    /**
     * 先生用問題登録処理
     * @throws Exception
     */
    @Test
    public void 先生用問題登録処理() throws Exception {

        QuestionForm form = new QuestionForm();
        form.setTitle("テストタイトル");
        form.setDescription("テスト説明");
        form.setInputNum(2);

        mockMvc.perform(post("/teacher/question/add")
                .flashAttr("questionForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"));

        Optional<QuestionBean> opt = questionRepository.findById(new Long(1));
        opt.ifPresent(questionBean -> {
            assertEquals(questionBean.getTitle(), form.getTitle());
            assertEquals(questionBean.getDescription(), form.getDescription());
            assertEquals(questionBean.getInputNum(), form.getInputNum());
        });
        // ifPresentOrElseの実装はJDK9からの様子
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用問題編集ページ表示
     * @throws Exception
     */
    @Test
    public void 先生用問題編集ページ表示() throws Exception {

        // DB状態
        // 問題：問題１
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(post("/teacher/question/edit")
                    .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/question/edit"))
                .andReturn();

        QuestionForm resultForm = (QuestionForm) result.getModelAndView()
                .getModel().get("questionForm");

        assertEquals(resultForm.getId(), "1");
        assertEquals(resultForm.getTitle(), "問題タイトル１");
        assertEquals(resultForm.getDescription(), "問題説明１");
        assertEquals(resultForm.getInputNum(), 2);
    }

    /**
     * 先生用問題編集処理
     * @throws Exception
     */
    @Test
    public void 先生用問題編集処理() throws Exception {

        // DB状態
        // 問題：問題１
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        QuestionForm form = new QuestionForm();
        form.setId("1");
        form.setTitle("問題タイトル１－２");
        form.setDescription("問題説明１ー２");
        form.setInputNum(0);

        mockMvc.perform(post("/teacher/question/editprocess")
                    .flashAttr("questionForm", form))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/question"));

        Optional<QuestionBean> opt = questionRepository.findById(new Long(1));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(questionBean -> {
            assertEquals(questionBean.getId(), new Long(1));
            assertEquals(questionBean.getTitle(), "問題タイトル１－２");
            assertEquals(questionBean.getDescription(), "問題説明１ー２");
            assertEquals(questionBean.getInputNum(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用問題削除処理
     * @throws Exception
     */
    @Test
    public void 先生用問題削除処理() throws Exception {

        // DB状態
        // 問題：問題１
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
