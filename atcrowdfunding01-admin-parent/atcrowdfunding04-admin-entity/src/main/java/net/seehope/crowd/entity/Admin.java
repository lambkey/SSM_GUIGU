package net.seehope.crowd.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    private Integer id;

    private String login_Acct;

    private String user_Pswd;

    private String user_Name;

    private String email;

    private String createTime;


}