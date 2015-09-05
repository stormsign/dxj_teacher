package com.dxj.teacher.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片实体
 */
public class UserBean extends BaseBean implements Serializable {
    public UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public class UserInfo implements Serializable{
        private String id; //用户id
        private String nickName;//用户昵称
        private String headUrl;//头像
        private String sex;//性别
        private Long type;//老师类型
        private String roleName;//老师类型名称
        private String mobile;//手机号码
        private String university; //大学名称
        private String major;//大学专业
        private String ability;//教学能力
        private String grades;//高考分数
        private String entranceTime;//入学时间
        private String championType;//状元等级
        private List<String> images = new ArrayList<String>();//相册
        private String school;//高中学校
        private String schoolProvince;//省份
        private String schoolCity;//城市
        private String livingCity;//现居城市
        private int schoolAge;//教龄
        private String dialect;//方言
        private String birthday;//生日
        private String nationality;//国籍
        private String remark;//自我描述
        private String experience;//过往经历
        private String result;//教学成果
        private String degrees;//学历
        private String horoscope;//星座
        private List<String> label = new ArrayList<String>();//个人标签
        private List<String> solveLabel = new ArrayList<String>();//擅长
//        private String grade;//年级
        private String jsz;//教师证
        private CardBean card;//学生证

        public void setCard(CardBean card) {
            this.card = card;
        }

        public CardBean getCard() {
            return card;
        }
        //        public void setCard(String card) {
//            this.card = card;
//        }
//
//        public void setGrade(String grade) {
//            this.grade = grade;
//        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setDegrees(String degrees) {
            this.degrees = degrees;
        }

        public void setDialect(String dialect) {
            this.dialect = dialect;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public void setJsz(String jsz) {
            this.jsz = jsz;
        }

        public void setLabel(List<String> label) {
            this.label = label;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public void setSchoolAge(int schoolAge) {
            this.schoolAge = schoolAge;
        }

        public void setSchoolCity(String schoolCity) {
            this.schoolCity = schoolCity;
        }

        public void setSchoolProvince(String schoolProvince) {
            this.schoolProvince = schoolProvince;
        }

        public void setSolveLabel(List<String> solveLabel) {
            this.solveLabel = solveLabel;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public void setHoroscope(String horoscope) {
            this.horoscope = horoscope;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public void setLivingCity(String livingCity) {
            this.livingCity = livingCity;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public List<String> getImages() {
            return images;
        }

        public Long getType() {
            return type;
        }

//        public String getCard() {
//            return card;
//        }
//
//        public String getGrade() {
//            return grade;
//        }

        public String getHeadUrl() {
            return headUrl;
        }

        public String getHoroscope() {
            return horoscope;
        }

        public String getId() {
            return id;
        }

        public String getLivingCity() {
            return livingCity;
        }

        public String getMobile() {
            return mobile;
        }


        public String getNickName() {
            return nickName;
        }

        public String getSchool() {
            return school;
        }

        public String getSex() {
            return sex;
        }

        public void setAbility(String ability) {
            this.ability = ability;
        }

        public String getAbility() {
            return ability;
        }

        public void setChampionType(String championType) {
            this.championType = championType;
        }

        public String getChampionType() {
            return championType;
        }

        public void setEntranceTime(String entranceTime) {
            this.entranceTime = entranceTime;
        }

        public String getEntranceTime() {
            return entranceTime;
        }


        public void setGrades(String grades) {
            this.grades = grades;
        }

        public String getGrades() {
            return grades;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getMajor() {
            return major;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setUniversity(String university) {
            this.university = university;
        }

        public String getUniversity() {
            return university;
        }

        public String getRemark() {
            return remark;
        }

        public int getSchoolAge() {
            return schoolAge;
        }

        public List<String> getLabel() {
            return label;
        }

        public List<String> getSolveLabel() {
            return solveLabel;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getDegrees() {
            return degrees;
        }

        public String getDialect() {
            return dialect;
        }

        public String getExperience() {
            return experience;
        }

        public String getJsz() {
            return jsz;
        }

        public String getNationality() {
            return nationality;
        }

        public String getResult() {
            return result;
        }

        public String getSchoolCity() {
            return schoolCity;
        }

        public String getSchoolProvince() {
            return schoolProvince;
        }
    }
}
