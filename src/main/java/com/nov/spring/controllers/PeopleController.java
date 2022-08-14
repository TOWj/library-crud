package com.nov.spring.controllers;


import com.nov.spring.dao.PersonDAO;
import com.nov.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personDAO.show(id);
        model.addAttribute("person", person);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        //Помещаем в модель нового пустого человека с помощью ModelAttribute
        //В new.html получаем доступ к пустому объекту Person
        return "people/new";
    }


    @PostMapping()//адреса нет, потому что мы переходим без указания страницы в people по post-запросу
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person);
        //Здесь мы будем создавать нового человека на основе переданных из people/new данных, и добавлять его в БД
        //После добавления, редиректимся в people, т.е. в метод index.
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")//Когда переходим по этому пути
    public String edit(Model model, @PathVariable("id") int id) {//Получаем от get-запроса id и модель
        model.addAttribute("person", personDAO.show(id));//кладем в модель по ключу person человека с указанным id
        return "people/edit";//Переходим в edit.html
    }

    @PatchMapping("/{id}")//Переходя по id в people из edit.html с patch-запросом
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id) {//сразу тащим объект Person и переменную id из адреса
        personDAO.update(person, id);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }

}