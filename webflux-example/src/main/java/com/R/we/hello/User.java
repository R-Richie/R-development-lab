package com.R.we.hello;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
public class User {

    @Id
    private Long id;
    private String username;
    private String address;
    //省略 getter/setter

}
