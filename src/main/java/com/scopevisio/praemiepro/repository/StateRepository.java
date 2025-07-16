package com.scopevisio.praemiepro.repository;

import com.scopevisio.praemiepro.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findOneByRegions_zipcode(String zipcode);
}
