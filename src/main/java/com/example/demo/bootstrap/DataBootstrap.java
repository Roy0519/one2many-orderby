package com.example.demo.bootstrap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Department;
import com.example.demo.domain.Person;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.PersonRepository;


@Component
public class DataBootstrap implements CommandLineRunner {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  DepartmentRepository departmentRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    var allDepartments = departmentRepository.findAll();
    for (var department : allDepartments) {
      for(var person : department.getPersons()){
        System.out.println(person.toString());
      }
    }
    test();
    checkData();
  }


  public void test() {
    Department d = new Department();
    d.setName("Design");

    Person p1 = new Person("Roy");
    p1.setDepartment(d);

    Person p2 = new Person("Jhon");
    p2.setDepartment(d);

    d.getPersons().add(p1);
    d.getPersons().add(p2);

    em.persist(p1);
    em.persist(p2);
    em.persist(d);
    
  }

  private void checkData() {

    //em.getTransaction().begin();

    Session session = em.unwrap(Session.class);
    session.doWork(new Work() {

      @Override
      public void execute(Connection con) throws SQLException {
        try {
          Statement st = con.createStatement();
          ResultSet mrs = con.getMetaData().getTables("mappingdb", null, null, new String[] { "TABLE" });
          while (mrs.next()) {
            String tableName = mrs.getString(3);
            System.out.println("\n\n\n\nTable Name: " + tableName);

            ResultSet rs = st.executeQuery("select * from " + tableName);
            ResultSetMetaData metadata = rs.getMetaData();
            while (rs.next()) {
              System.out.println(" Row:");
              for (int i = 0; i < metadata.getColumnCount(); i++) {
                System.out.println("    Column Name: " + metadata.getColumnLabel(i + 1) + ",  ");
                System.out.println("    Column Type: " + metadata.getColumnTypeName(i + 1) + ":  ");
                Object value = rs.getObject(i + 1);
                System.out.println("    Column Value: " + value + "\n");
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

    // em.getTransaction().commit();
    // em.close();

  }

}
