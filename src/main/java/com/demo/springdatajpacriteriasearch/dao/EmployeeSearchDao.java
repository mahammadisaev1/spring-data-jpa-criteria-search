package com.demo.springdatajpacriteriasearch.dao;

import com.demo.springdatajpacriteriasearch.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager entityManager;
    public List<Employee> findAllBySimpleQuery(
            String firstName,
            String lastName,
            String email
    ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // SELECT * FROM employees;
        Root<Employee> root = criteriaQuery.from(Employee.class);

        //prepare where clause
        //WHERE firstname LIKE '%a%'
        Predicate firstnamePredicate = criteriaBuilder
                .like(root.get("firstname"), "%"+ firstName+"%");

        Predicate lastnamePredicate = criteriaBuilder
                .like(root.get("lastname"), "%"+ lastName+"%");

        Predicate emailPredicate = criteriaBuilder
                .like(root.get("email"), "%"+ email+"%");
        Predicate orPredicate = criteriaBuilder.or(firstnamePredicate, lastnamePredicate);
        // final query SELECT * FROM employees WHERE firstname LIKE "%a%" or lastname LIKE "%a%" or email LIKE "%a%";

        var andEmailPredicate = criteriaBuilder.and(orPredicate, emailPredicate);
        criteriaQuery.where();

        criteriaQuery.where(andEmailPredicate);
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Employee> findAllByCriteria(
            SearchRequest request
    ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<Employee> root = criteriaQuery.from(Employee.class);
        if(request.getFirstname() != null){
            Predicate firstnamePredicate = criteriaBuilder
                    .like(root.get("firstname"), "%"+request.getFirstname()+"%");
            predicates.add(firstnamePredicate);
        }
        if(request.getLastname() != null){
            Predicate lastnamePredicate = criteriaBuilder
                    .like(root.get("lastname"), "%"+request.getLastname()+"%");
            predicates.add(lastnamePredicate);
        }
        if(request.getEmail() != null){
            Predicate emailPredicate = criteriaBuilder
                    .like(root.get("email"), "%"+request.getEmail()+"%");
            predicates.add(emailPredicate);
        }
        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))
        );
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
