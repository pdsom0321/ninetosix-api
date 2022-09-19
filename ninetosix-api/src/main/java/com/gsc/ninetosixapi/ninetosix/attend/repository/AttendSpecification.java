package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class AttendSpecification {
    public static Specification<Attend> betweenYesterdayAndToday(LocalDateTime yesterday, LocalDateTime today){
        return new Specification<Attend>() {
            @Override
            public Predicate toPredicate(Root<Attend> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("insertDate"),yesterday, today);
            }
        };
    }
}
