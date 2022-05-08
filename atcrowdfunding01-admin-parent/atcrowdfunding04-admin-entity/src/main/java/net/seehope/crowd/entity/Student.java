package net.seehope.crowd.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/3/13 20:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String stuName;
    private Integer stuAge;
    private String address;
    private List<Subject> subjects;
}
