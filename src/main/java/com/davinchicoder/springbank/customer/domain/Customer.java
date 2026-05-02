package com.davinchicoder.springbank.customer.domain;

import com.davinchicoder.springbank.audit.domain.AuditableDomain;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class Customer extends AuditableDomain {

    private String id;

    private Long version;

    private String name;

    private String email;

}
