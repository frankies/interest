/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.springboot.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Entity
public class Visitor {

  private long id;
  
  private String ip;
  
  private long times;
  
  
 
  @Column(length=50)
  public String getIp() {
    return ip;
  }
  /**
   * @param ip the ip to set
   */
  
  public void setIp(String ip) {
    this.ip = ip;
  }
  /**
   * @return the times
   */
  @Column(precision=10)
  public long getTimes() {
    return times;
  }
  /**
   * @param times the times to set
   */
  public void setTimes(long times) {
    this.times = times;
  }
  /**
   * @return the id
   */
  @Id
  @Column(precision=10)
  public long getId() {
    return id;
  }
  
  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }
  
  
  
}
