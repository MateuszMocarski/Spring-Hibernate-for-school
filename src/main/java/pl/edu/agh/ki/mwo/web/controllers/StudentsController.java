package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

	@RequestMapping(value="/Students")
    public String listStudents(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("studentWithClass", DatabaseConnector.getInstance().joinStudentWithClass());
    	model.addAttribute("studentWithSchool", DatabaseConnector.getInstance().joinStudentWithClassAndSchool());
    	
        return "studentsList";
	}
	
	@RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().joinClassWithSchool());
    	model.addAttribute("studentWithClass", DatabaseConnector.getInstance().joinStudentWithClass());
    	
        return "studentForm";    
    }
	
	@RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=true) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPESEL", required=false) String PESEL,
    		@RequestParam(value="studentSchoolClass", required=false) String classID,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(PESEL);
    	
    	
    	
    	DatabaseConnector.getInstance().addStudent(student, classID);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	model.addAttribute("studentWithClass", DatabaseConnector.getInstance().joinStudentWithClass());
       	model.addAttribute("studentWithSchool", DatabaseConnector.getInstance().joinStudentWithClassAndSchool());
    	model.addAttribute("message", "Nowy uczeń został dodany");
         	
    	return "studentsList";
    }
	
	@RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
       	model.addAttribute("studentWithClass", DatabaseConnector.getInstance().joinStudentWithClass());
       	model.addAttribute("studentWithSchool", DatabaseConnector.getInstance().joinStudentWithClassAndSchool());
    	model.addAttribute("message", "Uczeń został usunięty");
         	
    	return "studentsList";
    }
	
	@RequestMapping(value="/EditStudent", method=RequestMethod.POST)
	public String editStudent(@RequestParam(value="studentId", required=false) String studentId,
			Model model, HttpSession session) {
		if(session.getAttribute("userLogin")==null)
			return "redirect:/Login";
		
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("student", DatabaseConnector.getInstance().getStudent(studentId).get(0));
		model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().joinClassWithSchool());
		
		return "studentForm";
	}
	
	@RequestMapping(value="/UpdateStudent", method=RequestMethod.POST)
	public String updateStudent(@RequestParam(value="studentId", required = false) String studentId,
			@RequestParam(value="studentName", required=true) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPESEL", required=false) String PESEL,
    		@RequestParam(value="studentSchoolClass", required=false) String classID,
    		Model model, HttpSession session) {    	
    	
		if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
		
		Student student = DatabaseConnector.getInstance().getStudent(studentId).get(0);
		student.setName(name);
		student.setSurname(surname);
		student.setPesel(PESEL);
		
		DatabaseConnector.getInstance().addStudent(student, classID);
		model.addAttribute("student", DatabaseConnector.getInstance().getStudent(studentId).get(0));
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("studentWithClass", DatabaseConnector.getInstance().joinStudentWithClass());
    	model.addAttribute("studentWithSchool", DatabaseConnector.getInstance().joinStudentWithClassAndSchool());
    	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().joinClassWithSchool());
    	
    	return "studentsList";
	}			
}
