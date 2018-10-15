/******************************************************************************/
/* SYSTEM     :                                                       */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.my.mockito.sample;

import org.my.mockito.sample.model.ArticleDatabase;
import org.my.mockito.sample.model.ArticleListener;
import org.my.mockito.sample.model.User;

/**
 *
 * @author  
 * @version 1.0
 *
 */
public class ArticleManager {

    private User user;
    private ArticleDatabase database;

    public ArticleManager(User user, ArticleDatabase database) {
        super();
        this.user = user;
        this.database = database;
    }

    public void initialize() {
        database.addListener(new ArticleListener());
    }
}