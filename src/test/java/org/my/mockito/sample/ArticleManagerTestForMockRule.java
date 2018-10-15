/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.my.mockito.sample;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.my.mockito.sample.model.ArticleCalculator;
import org.my.mockito.sample.model.ArticleDatabase;
import org.my.mockito.sample.model.ArticleListener;
import org.my.mockito.sample.model.ConsumerUserProvider;
import org.my.mockito.sample.model.UserProvider;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
public class ArticleManagerTestForMockRule {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ArticleCalculator calculator;
    @Mock ArticleDatabase database;
    @Mock User user;

    @Spy private UserProvider userProvider = new ConsumerUserProvider();

    @InjectMocks private ArticleManager manager;

    @Test public void shouldDoSomething() {
        // calls addListener with an instance of ArticleListener
        manager.initialize();

        // validate that addListener was called
        verify(database).addListener(any(ArticleListener.class));
    }
}
