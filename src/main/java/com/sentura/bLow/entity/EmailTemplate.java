package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="email_template")
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailTemplateId;

    private String emailType;

    @Column(length = 60000, columnDefinition = "TEXT")
    private String template;

    private String subject;
    private Boolean active;
    private Date createDateTime = new Date();

    @Override
    public String toString() {
        return "EmailTemplate{" +
                "emailTemplateId=" + emailTemplateId +
                ", emailType='" + emailType + '\'' +
                ", template='" + template + '\'' +
                ", subject='" + subject + '\'' +
                ", active=" + active +
                ", createDateTime=" + createDateTime +
                '}';
    }
}