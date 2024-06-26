/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.email.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.kalsym.email.service.model.order.*;
import java.util.List;

/**
 *
 * @author saros
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class AccountVerificationEmailBody {

    public enum ActionType {
        EMAIL_VERIFICATION("Email Verification"),
        PASSWORD_RESET("Password Reset"),
        ACCOUNT_CREATED_NOTIFICATION("Congratulation! Your DeliverIn account is created.");
        
        public final String label;

        private ActionType(String label) {
            this.label = label;
        }
    }

    private ActionType actionType;

    private String link;

}
