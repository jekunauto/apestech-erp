package com.apestech.scm.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.apestech.oap.AbstractRopRequest;
import com.apestech.oap.annotation.IgnoreSign;
import org.hibernate.validator.constraints.NotBlank;



public class LogonRequest extends AbstractRopRequest {

    @NotNull
    @Pattern(regexp = "\\w{4,30}")
    private String userName;

    @IgnoreSign
    @NotBlank
    @Pattern(regexp = "\\w{6,30}")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

