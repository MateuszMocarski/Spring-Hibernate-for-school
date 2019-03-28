package pl.edu.agh.ki.mwo.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;

public class DatabaseConnector {
	
	protected static DatabaseConnector instance = null;
	
	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}
	
	Session session;

	protected DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance = null;
	}
	
	public Iterable<School> getSchools() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List schools = query.list();
		
		return schools;
	}
	
	public List<School> getSchool(String schoolId) {
		System.out.println(schoolId);
		Query q = session.createQuery("FROM School s WHERE s.id = " + schoolId);
		List<School> results = q.list();
		return results; 
	}
	
	public void addSchool(School school) {
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
	}
	
	public void deleteSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}

	public Iterable<SchoolClass> getSchoolClasses() {
		String hql = "FROM SchoolClass";
		Query query = session.createQuery(hql);
		List schoolClasses = query.list();
		
		return schoolClasses;
	}
	
	public void addSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(schoolClass);
		} else {
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
	}
	
	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
	
	public Iterable<Student> getStudents(){
		String hql = "FROM Student";
		Query query = session.createQuery(hql);
		List students = query.list();
		
		return students;
	}
	
	public void addStudent(Student student, String classID) {
		String hql = "FROM SchoolClass sc WHERE sc.id=" + classID;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		if(results.size() == 0) {
			session.save(student);
		} else {
			SchoolClass schoolClass = results.get(0);
			schoolClass.addStudent(student);
			session.save(schoolClass);
		}
		transaction.commit();
	}
	
	public void deleteStudent(String studentID) {
		String hql = "FROM Student S WHERE S.id=" + studentID;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student s : results) {
			session.delete(s);
		} 
		transaction.commit();
	}
	
	public List<SchoolClass> joinClassWithSchool() {
		String hql = "SELECT s, c FROM School s JOIN s.classes c";
		Query q = session.createQuery(hql);
		List<SchoolClass> classWithSchool = q.list();
		return classWithSchool;	
	}
	
	public List<SchoolClass> getClassesJoinSchool() {
		String hql = "SELECT c, s FROM School s JOIN s.classes c";
		Query query = session.createQuery(hql);
		List<SchoolClass> classes = query.list();

		return classes;
	}
	
	public List<Student> joinStudentWithClass(){
		String hql = "SELECT c, s FROM SchoolClass c JOIN c.students s";
		Query q = session.createQuery(hql);
		List<Student> studentWithClass = q.list();
		return studentWithClass;
	}

	public List<SchoolClass> getSchoolClass(String classId) {
		Query q = session.createQuery("FROM SchoolClass sc WHERE sc.id=" + classId);
		List<SchoolClass> results = q.list();
		
		return results;
	}
}
