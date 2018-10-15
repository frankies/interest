[[TOC]]

Mockito 单元测试的使用示例

##  Pom.xml 引入 `Mockito` jar库

```xml
<dependency>
    <groupId>org.mockito</groupId>
      <artifactId>mockito-all</artifactId>
      <version>1.10.19</version>
</dependency>
```  

## 代码编写

### 简单代码

- 引入类

```java
import static org.mockito.Mockito.*;
```

- Mock 某个类

```java
GoodsSearchService srv = Mockito.mock(GoodsSearchService.class);
```                

- Mock 某个类的调用方法
```java
        this.messageSource = Mockito.mock( MessageSource.class);
         
        
        Mockito.doAnswer(inv -> { 
//            toLog("^ messageSource.getMessage - 1ms");
//            logArgs(inv);
//            TimeUnit.MILLISECONDS.sleep(1); //1ms
            return "test-message-" + inv.getArguments().length; //1ms -> 0ms
        }).when(this.messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
```       
 
  以上是对 `MessageSource`这个类的 `getMessage` 方法的Mock，最后要指定输入参数的类型。
  注： `inv.getArguments` 这个方法可取得方法的输入参数列表


### 启动Mockito 对象的三种方法

- 1. 利用@Before注解的方法进行`MockitoAnnotations.initMocks`调用

```java
public class ArticleManagerTestForAnnotionInit  {

       @Mock ArticleCalculator calculator;
       @Mock ArticleDatabase database;
       @Mock User user;

       @Spy private UserProvider userProvider = new ConsumerUserProvider();

       @InjectMocks private ArticleManager manager;


       @Before
       public void init() {
           MockitoAnnotations.initMocks(this);
       }

       @Test public void shouldDoSomething() {
           // calls addListener with an instance of ArticleListener
           manager.initialize();

           // validate that addListener was called
           verify(database).addListener(any(ArticleListener.class));
       }
}
```

- 2. 利用`org.junit.Rule`注解

```java
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
```

- 3. 利用`org.mockito.runners.MockitoJUnitRunner`

```java
@RunWith(MockitoJUnitRunner.class)
public class ArticleManagerTestForRunner  {

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
```

### 例子代码

```java
        MockSettings m = Mockito.withSettings() 
                .verboseLogging();
        
        this.goodsSearchService = Mockito.mock(
                GoodsSearchService.class);
         

        final Map<String, String> goodsCache = new HashMap<>();
        Mockito.doAnswer(inv -> { 
            
            long time = 10;
            String gcde = (String) inv.getArguments()[1];
            String gn = "";
            if(goodsCache.containsKey(gcde)) {
                time = 0; 
            }else {
                goodsCache.put(gcde, "test-goods-" + gcde);
            }
            gn = goodsCache.get(gcde);
            
            toLog("* goodsSearchService.getGoodsName - " + time + "ms");
            TimeUnit.MILLISECONDS.sleep(time); //10ms
            return gn;
        }).when(this.goodsSearchService).getGoodsName(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        
        //
        this.messageSource = Mockito.mock( MessageSource.class);
         
        
        Mockito.doAnswer(inv -> { 
//            toLog("^ messageSource.getMessage - 1ms");
//            logArgs(inv);
//            TimeUnit.MILLISECONDS.sleep(1); //1ms
            return "test-message-" + inv.getArguments().length; //1ms -> 0ms
        }).when(this.messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
        
        //
        this.taxSearchService = Mockito.mock(TaxSearchService.class);
        Mockito.doAnswer(inv -> { 
            toLog("^ taxSearchService.convertTaxCdForSales - 0ms"); //1ms -> 0ms
//            logArgs(inv);
//            TimeUnit.MILLISECONDS.sleep(1); //1ms
            return getTaxType();
        }).when(this.taxSearchService).convertTaxCdForSales(Mockito.anyString(), Mockito.anyString());
                                 
        
        //
        this.corpDao = Mockito.mock(CorpDao.class);
        Mockito.doAnswer(inv -> {
            toLog("* CorpDao.searchCorp - 3ms?");
            logArgs(inv);
            TimeUnit.MILLISECONDS.sleep(3); //3ms
            return "temp";
        }).when(corpDao).searchCorp(Mockito.anyString());
                   
        // 
        this.crncyDao = Mockito.mock(CrncyDao.class); 
        final Map<String, BigDecimal> currencyCache = new HashMap<>();
        Mockito.doAnswer(inv -> {
            
            long time = 1;
            String cur = (String) inv.getArguments()[0];
            if(currencyCache.containsKey(cur)) {
                time = 0;
            }else {
                currencyCache.put(cur, BigDecimal.valueOf(2));
            }
            toLog("* CrncyDao.findDecimalPlacesbyCrncyCd - " + time + "ms?");
            logArgs(inv);
            TimeUnit.MILLISECONDS.sleep(time); //3ms
            return BigDecimal.valueOf(2);
        }).when(crncyDao).findDecimalPlacesbyCrncyCd(Mockito.anyString());     

        
        // 
        this.busPartnerDao = Mockito.mock(BusPartnerDao.class);
        Mockito.doAnswer(inv -> {
            toLog("* BusPartnerDao.searchBusPartnerNm - 1ms?");
            logArgs(inv);
            TimeUnit.MILLISECONDS.sleep(1); //3ms -> 1ms
            return "temp";
        }).when(busPartnerDao).searchBusPartnerNm(Mockito.anyString());  
        
        //
        this.invoiceSmyRcdgWkDao = Mockito.mock(InvoiceSmyRcdgWkDao.class);
        Mockito.doAnswer(inv -> {
            toLog("* invoiceSmyRcdgWkDao.searchSellSttlCondtnWdg1Count - 1ms");
            logArgs(inv);
            TimeUnit.MILLISECONDS.sleep(1); //10ms -> 1ms?
            
            return Collections.emptyList();
        }).when(invoiceSmyRcdgWkDao).searchSellSttlCondtnWdg1Count(Mockito.anyString(), Mockito.any(jp.co.toyotsu.jast.lx.lx99.batch.common.entity.InvoiceSmyRcdgWk.class));  
        
        
        
        // 
        Mockito.doAnswer(inv -> {
            toLog("* invoiceSmyRcdgWkDao.searchSellSttlCondtnWdg2Count - 1ms");
            logArgs(inv);
            TimeUnit.MILLISECONDS.sleep(1); //10ms -> 1ms
            return Collections.emptyList();
        }).when(invoiceSmyRcdgWkDao).searchSellSttlCondtnWdg2Count(Mockito.anyString(), Mockito.any(jp.co.toyotsu.jast.lx.lx99.batch.common.entity.InvoiceSmyRcdgWk.class));  
     
        // 
     
        
        
        ExecutionContext ec = Mockito.mock(ExecutionContext.class);
        Mockito.doAnswer(i -> { 
            return false;
        }).when(ec).containsKey(Mockito.anyString());
         
        ChunkContext cc =  Mockito.mock(ChunkContext.class);
        StepContext sc =  Mockito.mock(StepContext.class);
        StepExecution se =  Mockito.mock(StepExecution.class);
        Mockito.when(se.getExecutionContext()).thenReturn(ec);
        Mockito.when(sc.getStepExecution()).thenReturn(se);
        Mockito.when(cc.getStepContext()).thenReturn(sc);
```        


----

### @Mock 和 @Spy 的区别


当我们对@Mock的类（@Mock private OrderDao dao;）进行模拟方法时，会像下面这样去做：
 `when(dao.getOrder()).thenReturn("returened by mock ");` // 或者使用更为推荐的given方法

但如果想对@Spy的类（@Spy private PriceService ps;）进行模拟方法时，需要像下面一样去做：
 `doReturn("twotwo").when(ps).getPriceTwo();`

原因：

使用@Mock生成的类，所有方法都不是真实的方法，而且返回值都是NULL。

使用@Spy生成的类，所有方法都是真实方法，返回值都是和真实方法一样的。

所以，你用when去设置模拟返回值时，它里面的方法（dao.getOrder()）会先执行一次。

使用doReturn去设置的话，就不会产生上面的问题，因为有when来进行控制要模拟的方法，所以不会执行原来的方法。

----

### Mockito 对象的利用

```
`@Captor` 创建ArgumentCaptor
`@Spy` 可以代替spy(Object).
`@InjectMocks` 如果此注解声明的变量需要用到mock对象，mockito会自动注入mock或spy成员
```

```
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
```    
---
### Mockito 的局限性

[https://github.com/mockito/mockito/wiki/FAQ#what-are-the-limitations-of-mockito](https://github.com/mockito/mockito/wiki/FAQ#what-are-the-limitations-of-mockito)

 
---
### [最新示例代码](http://javadoc.io/page/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
---
## 参考
- [http://www.vogella.com/tutorials/Mockito/article.html](http://www.vogella.com/tutorials/Mockito/article.html)
- [http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html)
