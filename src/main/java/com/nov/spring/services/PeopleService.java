package com.nov.spring.services;

import com.nov.spring.models.Book;
import com.nov.spring.models.Person;
import com.nov.spring.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional(readOnly = false)
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
        //этот метод и сохраняет и обновляет, просто заменит на нужные значения.
        //именно поэтому мы сначала задаем id переданному Person
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            List<Book> books = person.get().getBooks();
            Hibernate.initialize(books);//необязательно, делаем на всякий случай

            //Проверка просрочки книг
            books.forEach(book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillis > 864000000) {
                    book.setExpired(true); //просрочена
                }
            });
            return books;
        } else {
            return Collections.emptyList();
        }
    }

}