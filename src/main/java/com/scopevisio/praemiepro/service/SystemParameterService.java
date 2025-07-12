package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.SystemParameter;
import com.scopevisio.praemiepro.repository.SystemParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemParameterService {

    @Autowired
    private SystemParameterRepository systemParameterRepository;

    public SystemParameter getSystemParameter() {
        return systemParameterRepository.findById(1L).orElseThrow();
    }
}
