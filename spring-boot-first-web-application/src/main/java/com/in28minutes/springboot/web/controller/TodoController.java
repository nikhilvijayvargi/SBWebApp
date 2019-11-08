package com.in28minutes.springboot.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.service.TodoService;

@Controller
@SessionAttributes("name")
public class TodoController {

	@Autowired
	TodoService todoService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@RequestMapping(value = "/list-todos", method = RequestMethod.GET)
	public String showTodos(ModelMap modelMap) {
		// now since we are using @SessionAttributes then no need to hard code the value
		// i/e in28Minutes
		// we can get it directly from session
		// modelMap.put("todos", todoService.retrieveTodos("in28Minutes"))od
		// Below is the code to get the value from session and passing it to the ToDo
		// Service to call the method
		// String name = (String) modelMap.get("name");
		String name = getLoggedInUserName(modelMap);
		modelMap.put("todos", todoService.retrieveTodos(name));
		// modelMap.put("todos", todoService.retrieveTodos("password"));
		return "list-todos";
	}

	private String getLoggedInUserName(ModelMap model) {
		// We were returning using the Session Attribute, Now we are commenting it out
		// and using Spring security to get user details
		// return (String) model.get("name");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}

		return principal.toString();
	}

	@RequestMapping(value = "/add-todo", method = RequestMethod.GET)
	public String showAddTodoPage(ModelMap model) {
		// We are adding a default todo list item when the add-todo url is invoked
		model.addAttribute("todo", new Todo(0, getLoggedInUserName(model), "Default Desc", new Date(), false));
		// we are returning the view i.e.. todo.jsp
		return "todo";
	}

	@RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		if(id==1)
			throw new RuntimeException("Something went wrong");
		todoService.deleteTodo(id);
		return "redirect:/list-todos";
	}

	@RequestMapping(value = "/add-todo", method = RequestMethod.POST)
	public String addTodo(ModelMap modelMap, @Valid Todo todo, BindingResult result) {
		// BindingResult object result will have info about @Valid if it has any errors
		// If it is having any error we are sending the user to the same page i.e todo
		if (result.hasErrors()) {
			return "todo";
		}
		todoService.addTodo(getLoggedInUserName(modelMap), todo.getDesc(), todo.getTargetDate(), false);
		// todoService.addTodo((String) modelMap.get("name"), todo.getDesc(),
		// todo.getTargetDate(), false);
		// we are redirecting to the view list-todos after adding it the view i.e..
		// list-todos.jsp
		return "redirect:/list-todos";
	}

	@RequestMapping(value = "/update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
		Todo todo = todoService.retrieveTodo(id);
		model.put("todo", todo);
		return "todo";
	}

	@RequestMapping(value = "/update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		if (result.hasErrors()) {
			return "todo";
		}

		todo.setUser(getLoggedInUserName(model));

		todoService.updateTodo(todo);

		return "redirect:/list-todos";
	}
}
