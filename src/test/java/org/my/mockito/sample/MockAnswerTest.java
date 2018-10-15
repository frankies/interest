/******************************************************************************/
/* SYSTEM     :                                                       */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.my.mockito.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

/**
 *
 * @author  
 * @version 1.0
 *
 */
public class MockAnswerTest {

//    @Captor
//    private ArgumentCaptor<String> captor;

    @Test
    public final void answerTest() {

        List<String> ss = Arrays.asList("someElement_test", "someElement");
        final List<String> list = mock(List.class);

        list.addAll(ss);
        // with doAnswer():
        Mockito.doAnswer(m -> false).when(list).add(anyString());
//        Mockito.when(list.add(anyString())).thenAnswer(m -> false);


        assertFalse(list.add("aa"));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(list).add(captor.capture());
        String val = captor.getValue();
        assertEquals(val, captor.getValue());


    }

    @Test

    public void testSpy() {

        // Lets mock a LinkedList

        List list = new LinkedList();

        list.add("yes");

        List spy = Mockito.spy(list);

        // You have to use doReturn() for stubbing

        assertEquals("yes", spy.get(0));

        Mockito.doReturn("foo").when(spy).get(0);

        assertEquals("foo", spy.get(0));

    }

    @Test(expected = IndexOutOfBoundsException.class)

    public void testSpy2() {

        // Lets mock a LinkedList

        List list = new LinkedList();

        List spy = Mockito.spy(list);

        // this would not work

        // real method is called so spy.get(0)

        // throws IndexOutOfBoundsException (list is still empty)

        Mockito.when(spy.get(0)).thenReturn("foo");

        assertEquals("foo", spy.get(0));

    }

}
