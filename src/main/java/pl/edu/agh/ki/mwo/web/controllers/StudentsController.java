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
    	
        return "studentsList";
	}
	
	@RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
        return "studentForm";    
    }
	
	@RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=true) String name,
    		@RequestParam(value="studentSurname", required=true) String surname,
    		@RequestParam(value="studentPESEL", required=true) String PESEL,
    		@RequestParam(value="studentSchool", required=false) String schoolID,
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
    	model.addAttribute("message", "Nowy uczeń został dodany");
         	
    	return "studentsList";
    }
	
	@RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteSchool(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Uczeń został usunięty");
         	
    	return "studentsList";
    }
}
