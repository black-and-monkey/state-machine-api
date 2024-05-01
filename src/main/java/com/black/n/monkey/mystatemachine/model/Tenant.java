package com.black.n.monkey.mystatemachine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Tenant {

    private String tenantId;
    private String name;

}
