package com.nov.spring.dao;

import com.nov.spring.models.Book;
import com.nov.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.queryForObject("select * from person where id = ? limit 1",
                new BeanPropertyRowMapper<>(Person.class), id);
    }

    public Optional<Person> show(String fullName) {
        return jdbcTemplate.query("select * from person where full_name = ?",
                new BeanPropertyRowMapper<>(Person.class), fullName).stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("insert into person(full_name, year_of_birth) values (?, ?)",
                person.getFullName(), person.getYearOfBirth());
    }

    public void update(Person updatedPerson, int id) {
        jdbcTemplate.update("update person set full_name=?, year_of_birth=? where id=?",
                updatedPerson.getFullName(), updatedPerson.getYearOfBirth(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from person where id=?",id);
    }

    public List<Book> getBooksById(int id) {
        return jdbcTemplate.query("select * from book where id=?", new BeanPropertyRowMapper<>(Book.class), id);
    }
}
