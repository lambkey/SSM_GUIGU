package net.seehope.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author JoinYang
 * @date 2022/3/30 21:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVo {

    @Pattern(regexp = "^[1-9][0-9]{4,10}$",message = "要求输入长度为5-11位数字组合账号")
    private String login_Acct;

    private String user_Pswd;

    private String user_Name;

    private String email;
}
