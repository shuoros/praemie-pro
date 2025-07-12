package com.scopevisio.praemiepro.repository;

import com.scopevisio.praemiepro.domain.SystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {
}
