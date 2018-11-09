/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Data
@AllArgsConstructor
public class User implements Serializable{


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
}
