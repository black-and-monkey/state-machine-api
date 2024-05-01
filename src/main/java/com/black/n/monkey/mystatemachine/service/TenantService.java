package com.black.n.monkey.mystatemachine.service;


import com.black.n.monkey.mystatemachine.model.Tenant;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TenantService {

    private final Set<Tenant> persistance = new HashSet();

    public void addTenant (Tenant tenant) {
        persistance.add(tenant);
    }

    public Set<Tenant> findTenants () {
        return persistance;
    }
}

