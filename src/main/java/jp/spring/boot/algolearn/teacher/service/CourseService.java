package jp.spring.boot.algolearn.teacher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.CourseRepository;
import jp.spring.boot.algolearn.repository.UserRepository;
import jp.spring.boot.algolearn.teacher.form.CourseForm;
import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.ClassCourseBean;
import jp.spring.boot.algolearn.bean.CourseBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.bean.UserCourseBean;
import jp.spring.boot.algolearn.config.RoleCode;

/**
 * 先生用コースerviceクラス（teacher course Service Class）
 * 
 * @author tejc999999
 *
 */
@Service
public class CourseService {

    /**
     * コース用リポジトリ(course repository)
     */
    @Autowired
    CourseRepository courseRepository;

    /**
     * クラス用リポジトリ(class repository)
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザー用リポジトリ(user repository)
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全てのコースを取得
     * @return 全コースのリスト
     */
    public List<CourseForm> findAll() {
        
        List<CourseForm> courseFormList = new ArrayList<CourseForm>();

        // 全てのコースBeanを取得し、リストに格納する
        for (CourseBean courseBean : courseRepository.findAll()) {
            CourseForm courseForm = new CourseForm();
            courseForm.setId(String.valueOf(courseBean.getId()));
            courseForm.setName(courseBean.getName());
            courseFormList.add(courseForm);
        }
        
        return courseFormList;
    }
    
    /**
     * 全てのクラスを取得
     * @return 全クラスのマップ
     */
    public Map<String, String> findAllClass() {
        Map<String, String> classMap = new HashMap<>();
        // 全てのクラスBeanを取得する
        List<ClassBean> classBeanList = classRepository.findAll();
        // 全てのクラスBeanの、クラスIDとクラス名の対応マップを作成する
        if (classBeanList != null) classBeanList.forEach(bean -> classMap.put(String.valueOf(bean.getId()), bean.getName()));
        
        return classMap;
    }

    /**
     * 全ての学生を取得
     * @return 全学生のマップ
     */
    public Map<String, String> findAllStudent() {
        Map<String, String> userMap = new HashMap<>();
        // 全ての学生のユーザーBeanを取得する
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getId());
        if (userBeanList != null) userBeanList.forEach(bean -> userMap.put(bean.getId(), bean.getName()));
        
        return userMap;
    }
    
    /**
     * 全ての学生を取得（クラス所属ユーザを除外する）
     * @return 全学生のマップ
     */
    public Map<String, String> findAllStudent(List<String> classIdList) {

        // 全ての学生の情報をマップに取得
        Map<String, String> userMap = findAllStudent();
        
        // クラスに所属するユーザをマップから削除する
        if (classIdList != null) {
            List<String> removeUserLlist = new ArrayList<>();
            if (classIdList != null)
                classIdList.forEach(classId -> {
                    Optional<ClassBean> opt = classRepository.findById(Long.parseLong(classId));
                    opt.ifPresent(classBean -> removeUserLlist.addAll(classBean.getUserIdList()));
                });
            // 選択済みクラス所属ユーザを除外
            if (removeUserLlist != null && userMap != null) removeUserLlist.forEach(userId -> userMap.remove(userId));
        }
        
        return userMap;
    }
    
    /**
     * コースを保存する
     * @param form コースForm
     * @return 保存済みコースForm
     */
    public CourseForm save(CourseForm form) {
        
        CourseBean courseBean = new CourseBean();
        // ID、名前をBeanに設定する
        String courseId = form.getId();
        if(courseId != null && !courseId.equals("")) {
            courseBean.setId(Long.parseLong(courseId));
        }
        courseBean.setName(form.getName());

        // クラス、コース中間情報をBeanに設定する
        courseBean.clearClassCourseBean();
        List<String> classIdList = form.getClassCheckedList();
        if(classIdList != null) {
            for(int i = 0; i < classIdList.size(); i++ ) {
                Optional<ClassBean> optClass = classRepository.findById(Long.parseLong(classIdList.get(i)));
                List<Long> idList = new ArrayList<>();
                optClass.ifPresent(classBean -> {
                    idList.add(classBean.getId());
                });
                ClassCourseBean classCourseBean = new ClassCourseBean();
                if(courseId != null && !courseId.equals("")) {
                    classCourseBean.setCourseId(Long.parseLong(courseId));
                }
                classCourseBean.setClassId(idList.get(0));
                courseBean.addClassCourseBean(classCourseBean);
            }
        }

        // ユーザー、コース中間情報をBeanに設定する
        List<String> userIdList = form.getUserCheckedList();
        if(userIdList != null) {
            for(int i = 0; i < userIdList.size(); i++ ) {
                Optional<UserBean> optClass = userRepository.findById(userIdList.get(i));
                List<String> idList = new ArrayList<>();
                optClass.ifPresent(userBean -> {
                    idList.add(userBean.getId());
                });
                UserCourseBean userCourseBean = new UserCourseBean();
                if(courseId != null && !courseId.equals("")) {
                    userCourseBean.setCourseId(Long.parseLong(courseId));
                }
                userCourseBean.setUserId(idList.get(0));
                courseBean.addUserCourseBean(userCourseBean);
            }
        }

        // DBに保存する
        courseBean = courseRepository.save(courseBean);
        
        // BeanのデータをFormにコピーする
        CourseForm resultForm = new CourseForm();
        resultForm.setId(String.valueOf(courseBean.getId()));
        resultForm.setName(courseBean.getName());
        resultForm.setClassCheckedList(courseBean.getClassIdList());
        resultForm.setUserCheckedList(courseBean.getUserIdList());

        return resultForm;
    }

    /**
     * 選択済みのクラス、ユーザーの情報を設定する
     * @param id コースID
     * @return コースForm
     */
    public CourseForm checkClassAndUser(String id) {

        CourseForm courseForm = new CourseForm();
        Optional<CourseBean> optBean = courseRepository.findById(Long.parseLong(id));
        optBean.ifPresent(courseBean -> {
            courseForm.setId(String.valueOf(courseBean.getId()));
            courseForm.setName(courseBean.getName());
            courseForm.setClassCheckedList(courseBean.getClassIdList());
            courseForm.setUserCheckedList(courseBean.getUserIdList());      
        });
        
        return courseForm;
    }
    
    /**
     * コースを削除する
     * @param id コースID
     */
    public void delete(String id) {
        CourseBean courseBean = new CourseBean();
        courseBean.setId(Long.parseLong(id));
        courseRepository.delete(courseBean);
    }
}
