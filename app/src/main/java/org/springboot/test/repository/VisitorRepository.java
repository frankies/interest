/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.springboot.test.repository;

import org.springboot.test.entity.Visitor;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
public interface VisitorRepository extends CrudRepository<Visitor, Long> {
  
  Visitor findByIp(String ip);
}
