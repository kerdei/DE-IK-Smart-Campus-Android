package hu.unideb.smartcampus.task.login;

import hu.unideb.smartcampus.pojo.login.ActualUserInfo;
import hu.unideb.smartcampus.task.pojo.ReturnPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data

class BasicAuthReturnPojo extends ReturnPojo {
    private ActualUserInfo actualUserInfo;

    BasicAuthReturnPojo(Integer statusCode, ActualUserInfo actualUserInfo) {
        super(statusCode);
        this.actualUserInfo = actualUserInfo;
    }
}
