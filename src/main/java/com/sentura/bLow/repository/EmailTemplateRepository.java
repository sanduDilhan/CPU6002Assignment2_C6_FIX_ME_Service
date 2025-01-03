package com.sentura.bLow.repository;

import com.sentura.bLow.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate,Long> {

    EmailTemplate findByEmailTypeAndActiveTrue(String emailType);
}
