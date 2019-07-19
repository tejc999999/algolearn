package jp.spring.boot.algolearn.teacher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.bean.UserClassBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.UserRepository;
import jp.spring.boot.algolearn.teacher.form.ClassForm;

/**
 * 先生用クラスServiceクラス（teacher class Service Class）
 * 
 * @author tejc999999
 *
 */
@Service
public class ClassService {

    /**
     * クラス用リポジトリ(class repository)
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザー用リポジトリ(class repository)
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全てのクラス情報を取得する
     * @return クラスFormリスト
     */
    public List<ClassForm> findAll() {

        List<ClassForm> classFormList = new ArrayList<ClassForm>();

        for (ClassBean classBean : classRepository.findAll()) {
            ClassForm classForm = new ClassForm();
            classForm.setId(String.valueOf(classBean.getId()));
            classForm.setName(classBean.getName());
            classFormList.add(classForm);
        }
        
        return classFormList;
    }
    
    /**
     * 全ての学生について、IDと名前の対応マップを返す
     * @return 学生のIDと名前の対応マップ
     */
    public Map<String, String> getUserIdMap() {
        
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        
        if (userBeanList != null)
            userBeanList.forEach(userBean -> {
                userMap.put(userBean.getId(), userBean.getName());
            });
        
        return userMap;
    }
    
    /**
     * クラスを保存する（関連する先のテーブルは更新しない）
     * @param form 登録するクラス情報
     * @return  登録したクラス情報
     */
    public ClassForm save(ClassForm form) {

        // Formの値を（クラスの情報）Beanにコピーする
        ClassBean classBean = new ClassBean();
        if(form.getId() != null && !form.getId().equals("")) {
            classBean.setId(Long.parseLong(form.getId()));
        }
        classBean.setName(form.getName());
        
        // Formの値（クラスに所属するユーザーの情報）をBeanにコピーする
        List<String> userIdList = form.getUserCheckedList();
        if(userIdList != null) {
            for(int i = 0; i < userIdList.size(); i++) {
                UserClassBean userClassBean = new UserClassBean();
                userClassBean.setUserId(userIdList.get(i));
                if(form.getId() != null && !form.getId().equals("")) {
                    userClassBean.setClassId(Long.parseLong(form.getId()));
                }
                classBean.addUserClassBean(userClassBean);
            }
        }
        // DBに保存する
        classBean = classRepository.save(classBean);
        
        // Beanの値をFormにコピーする
        ClassForm resultForm = new ClassForm();
        resultForm.setId(String.valueOf(classBean.getId()));
        resultForm.setName(classBean.getName());
        resultForm.setUserCheckedList(classBean.getUserIdList());

        return resultForm;
    }
    
    
    /**
     * クラス情報を取得する
     * @return クラスForm
     */
    public ClassForm findById(String id) {
        
        ClassForm classForm = new ClassForm();
        Optional<ClassBean> optClass = classRepository.findById(Long.parseLong(id));
        optClass.ifPresent(classBean -> {
            classForm.setId(String.valueOf(classBean.getId()));
            classForm.setName(classBean.getName());
            classForm.setUserCheckedList(classBean.getUserIdList());
        });
        
        return classForm;
    }
    
    /**
     * クラス削除
     * @param id クラスID
     */
    public void delete(String id) {
        ClassBean classBean = new ClassBean();
        classBean.setId(Long.parseLong(id));
        classRepository.delete(classBean);
    }
}
