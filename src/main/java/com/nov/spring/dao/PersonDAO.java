package com.nov.spring.dao;

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
        return jdbcTemplate.queryForObject("select * from person where id = ? limit 1"
                , new BeanPropertyRowMapper<>(Person.class), id);
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("select * from person where email = ?"
                , new BeanPropertyRowMapper<>(Person.class), email).stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("insert into person(name, age, email, address) values(?, ?, ?, ?)"
                , person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("update person set name=?, age=?, email=?, address=? where id=?"
                , updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from person where id=?", id);
    }

    //////////////////////////////////////////////////////////////////
    // Batch update testing
    //////////////////////////////////////////////////////////////////

    public void testMultipleUpdate() {
        List<Person> people = create1000People();
        long begin = System.currentTimeMillis();
        for (Person person : people) {
            jdbcTemplate.update("insert into person values(?, ?, ?, ?)", person.getId(), person.getName()
                    , person.getAge(), person.getEmail(), person.getAddress());
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - begin));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();
        long begin = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("insert into person values(?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, people.get(i).getId());
                preparedStatement.setString(2, people.get(i).getName());
                preparedStatement.setInt(3, people.get(i).getAge());
                preparedStatement.setString(4, people.get(i).getEmail());
                preparedStatement.setString(5, people.get(i).getAddress());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - begin));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "test" + i + "@mail.ru", i + " street"));
        }
        return people;
    }
}
