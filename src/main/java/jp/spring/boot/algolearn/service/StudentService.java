package jp.spring.boot.algolearn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.StudentForm;
import jp.spring.boot.algolearn.repository.UserRepository;

/**
 * 先生用学生Serviceクラス（teacher student Service Class）
 * 
 * @author tejc999999
 *
 */
@Service
public class StudentService {

    /**
     * ユーザー用リポジトリ(class repository)
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全ての学生を取得する
     * @return 全ての学生Formリスト
     */
    public List<StudentForm> findAll() {

        List<StudentForm> studentFormList = new ArrayList<StudentForm>();

        for (UserBean userBean : userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString())) {
            StudentForm userForm = new StudentForm();
            BeanUtils.copyProperties(userBean, userForm);
            studentFormList.add(userForm);
        }
        
        return studentFormList;
    }
    
    /**
     * 学生を保存する
     * @param form 学生Form
     * @return 登録済み学生Form
     */
    public StudentForm save(StudentForm form) {

        UserBean userBean = new UserBean();
        BeanUtils.copyProperties(form, userBean);
        userBean = userRepository.save(userBean);
        StudentForm resultForm = new StudentForm();
        BeanUtils.copyProperties(userBean, form);
        
        return resultForm;
    }
    
    /**
     * 学生を取得する
     * @param id ユーザーID
     * @return 学生Form
     */
    public StudentForm findById(String id) {

        StudentForm studentForm = new StudentForm();

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> BeanUtils.copyProperties(userBean, studentForm));
        
        return studentForm;
    }
    
    /**
     * 学生を削除する
     * @param id ユーザーID
     */
    public void delete(String id) {
        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> userRepository.delete(userBean));
    }
}
