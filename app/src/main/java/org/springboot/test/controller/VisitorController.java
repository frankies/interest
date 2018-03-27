/******************************************************************************/
/* SYSTEM : YNA2.0 */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.springboot.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.springboot.test.entity.Visitor;
import org.springboot.test.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author YMSLX
 * @version 1.0
 *         
 */
@RestController
public class VisitorController {
  
  @Autowired
  private VisitorRepository repository;
  
  @RequestMapping("/")
  public String index(HttpServletRequest request) {
    String ip = request.getRemoteAddr();
    Visitor visitor = repository.findByIp(ip);
    if (visitor == null) {
      visitor = new Visitor();
      visitor.setIp(ip);
      visitor.setTimes(1);
    } else {
      visitor.setTimes(visitor.getTimes() + 1);
    }
    repository.save(visitor);
    return "I have been seen ip " + visitor.getIp() + " " + visitor.getTimes() + " times.";
  }
}
