package net.seehope.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JoinYang
 * @date 2022/3/13 20:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String subName;
    private Integer subScore;
}
