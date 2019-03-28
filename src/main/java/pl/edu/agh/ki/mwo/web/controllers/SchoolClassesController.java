package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class SchoolClassesController {

    @RequestMapping(value="/SchoolClasses")
    public String listSchoolClass(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());
    	
        return "schoolClassesList";    
    }
    
    @RequestMapping(value="/AddSchoolClass")
    public String displayAddSchoolClassForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	model.addAttribute("schoolList", DatabaseConnector.getInstance().getSchools());
       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());
       	
        return "schoolClassForm";    
    }

    @RequestMapping(value = "/CreateSchoolClass", method = RequestMethod.POST)
	public String createSchoolClass(@RequestParam(value = "classProfile", required = false) String profile,
			@RequestParam(value = "classCurrentYear", required = false) String currentYear,
			@RequestParam(value = "classStartYear", required = false) String startYear,
			@RequestParam(value = "classSchool", required = false) String schoolId, Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setProfile(profile);
		schoolClass.setCurrentYear(Integer.parseInt(currentYear));
		schoolClass.setStartYear(Integer.parseInt(startYear));

		DatabaseConnector.getInstance().addSchoolClass(schoolClass, schoolId);
		model.addAttribute("classes", DatabaseConnector.getInstance().getClassesJoinSchool());
		model.addAttribute("message", "Dodano nową klasę");
		model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());
         	
    	return "schoolClassesList";
    }
    
    @RequestMapping(value="/DeleteSchoolClass", method=RequestMethod.POST)
    public String deleteSchoolClass(@RequestParam(value="schoolClassId", required=false) String schoolClassId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteSchoolClass(schoolClassId);    	
       	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
       	model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());
    	model.addAttribute("message", "Klasa została usunięta");
         	
    	return "schoolClassesList";
    }
    
    @RequestMapping(value = "/EditSchoolClass", method = RequestMethod.POST)
	public String editSchoolClass(@RequestParam(value = "schoolClassId", required = false) String classId,
			@RequestParam(value = "schoolId", required = false) String schoolId, Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		model.addAttribute("schoolID", schoolId);
		model.addAttribute("schoolClass", DatabaseConnector.getInstance().getSchoolClass(classId));
		model.addAttribute("schoolList", DatabaseConnector.getInstance().getSchools());
		model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
		model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());

		return "schoolClassForm";
	}
    
    @RequestMapping(value = "/UpdateSchoolClass", method = RequestMethod.POST)
	public String updateSchoolClass(@RequestParam(value = "classId", required = false) String classId,
			@RequestParam(value = "classProfile", required = false) String classProfile,
			@RequestParam(value = "classCurrentYear", required = false) String currentYear,
			@RequestParam(value = "classStartYear", required = false) String startYear,
			@RequestParam(value = "classSchool", required = false) String schoolId, Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";
		
		SchoolClass sc = DatabaseConnector.getInstance().getSchoolClass(classId).get(0);
		sc.setProfile(classProfile);
		sc.setCurrentYear(Integer.parseInt(currentYear));
		sc.setStartYear(Integer.parseInt(startYear));

		School s = DatabaseConnector.getInstance().getSchool(schoolId).get(0);
		s.addClass(sc);

		DatabaseConnector.getInstance().addSchool(s);
		model.addAttribute("classes", DatabaseConnector.getInstance().joinClassWithSchool());
		model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
		model.addAttribute("schoolClassSchool", DatabaseConnector.getInstance().getClassesJoinSchool());
		model.addAttribute("message", "Wyedytowano klasę");

		return "schoolClassesList";
	}


}