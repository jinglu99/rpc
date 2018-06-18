# RPC使用手册

[toc]

## 快速搭建

本RPC框架的配置非常方便，按照传统，我们使用Hello World作为我们的第一个示例项目，首先我们需要在开发环境中安装Zookeeper，该部分不是本文重点，顾不在此介绍。在这个示例项目中，我们需要创建三个工程，分别是：SDK, Provider, Consumer(名字并不固定)， SDK指定了服务发布者提供的接口，服务调用方通过SDK调用远程服务，Provider即为服务的发布者，在此工程中实现服务，Consumer是服务的调用者。

### 定义服务接口

首先我们需要定义服务接口，我们将本项目编译后的 rpc.jar 导入SDK工程，然后我们新建一个`HelloService`接口：

```
public interface HelloService {
    String sayHello(String name);
}
```

### 在服务提供方实现接口

定义完服务接口之后，我们需要在Provider中对接口进行实现，并将其发布。

首先我们需要新建一个`HelloServiceImpl`类对`HelloService`接口进行实现：

```
@Provider("helloService")
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        String str = "hello " + name;
        System.out.println(str);
        return str;
    }
}
```

本框架通过使用Provider表标该服务需要被暴露给调用方，所以需要添加@Provider注解，这个注解需要一个String类型的服务id。

接着，我们就需要将该服务发布， 在发布之前，我们需要提供一些配置：

```
rpc.register.url=127.0.0.1	
rpc.register.port=2181 
rpc.provider.package=com.test	//provider 包名
```

以上是发布一个服务所需要的最简配置，分别声明了注册中心的IP地址，端口，和Provider工程的包名。

最后我们需要调用RPC框架的初始化方法：

```
 public static void main(String[] args) throws ServiceExportFailedException, IOException {
        Container container = new Container();
        container.init();

        while (true);
}
```

通过Container的init()方法，可以将实现的HelloService接口发布，由于init()是一个异步的方法，所以需要第5行代码防止主线程退出。

### 在服务调用方使用远程服务

最后，我们需要使用已经发布的服务，同样的，我们需要添加一些配置：

```
rpc.register.url=127.0.0.1
rpc.register.port=2181
```

我们需要提供注册中心的地址与端口号。然后，通过Proxy获得远程服务代理：

```
 public static void main(String[] args) throws FailToGenerateInvokerException {
        Proxy proxy = new CglibReferProxy();
        HelloService helloService = proxy.getInstance(HelloService.class);
        System.out.println(helloService.sayHello("test"));
  }
```

至此，一个最简单的RPC调用模型，已经搭建完成。

## 高级配置

对于不同的运行环境，不同的业务场景我们提供了一些可供开发者选择的配置，如序列化方式、负载均衡算法、超时重试机制、连接数以及可以根据服务器配置可以灵活配置业务线程组大小。

### 序列化

我们预制了5种序列化的方式：`Fastjson`、`Jackson`、`Jdk`、`Kryo`、`Protobuf`等，默认使用的是jdk，如果需要切换序列化方式，只需要在配置文件中声明如下配置 (Provider和Consumer需要使用相同配置)。

```
rpc.serialize.type=...
```

用户也可以根据实际的使用场景，编写自己的序列化方式，只需要实现`RPCSerializer`接口（在本框架中，所有的用户自定义组件类的类名采用驼峰命名法，且必须以接口名结尾，在本例中，我们使用RPCSerializer）,并声明组件所在的包名。

例如在sdk包中，新建工具类`TestRPCSerializer` 实现`RPCSerializer`接口：

```
public class TestRPCSerializer implements RPCSerializer {
    public byte[] serialize(Object o) throws SerializeException {
        //在此实现序列化功能
        return new byte[0];
    }

    public <T> T deserialize(byte[] bytes, Class aClass) throws SerializeException {
        //在此实现反序列化功能
        return null;
    }
}
```

完成了自定义序列化组件的编写后，我们可以在配置文件中直接声明为`test`:

```
rpc.serialize.type=test
rpc.packages=com.test; //组件所在的包名，多个包用;隔开
```



### 负载均衡

负载均衡算法的选择，可以通过修改`rpc.consumer.load`属性。我们默认使用的是随机算法。同样，用户可以自定义自己的选择算法, 只需实现`LoadBalance`接口：

```
public class TestLoadBalance implements LoadBalance {
    public RandomLoadBalance() {
    }

    public Object select(Invocation invocation, Set urls) {
        ArrayList tmp = new ArrayList(urls);
        if(tmp.size() > 0) {
            int index = (int)(Math.random() * (double)tmp.size());
            return tmp.get(index);
        } else {
            return null;
        }
    }
}
```

`select`方法有2个参数，`invocation`是记录此次远程调用信息的容器，`urls`则是候选目标服务器。

在配置文件中添加：

```
rpc.consumer.load=test
rpc.packages=com.test; //组件所在的包名，多个包用;隔开
```



### 超时重试

在容错方面，我们提供了`rpc.provider.timeout`和`rpc.consumer.retry`这2个属性，用户可以设置超时时间和重试次数，默认超时时间为3000ms，默认重试次数为3次。

### 连接稳定性

为了保证调用的稳定性，我们提供了可以修改每个Consumer和Provider的连接数、已经连接的断开重连机制：

```
rpc.consumer.connections=... //在此可修改连接数，默认1个
rpc.netty.retry=...	//当连接断开时，尝试重新连接的次数，默认3次
rpc.netty.interval=...	//每次重连的间隔，默认1000ms
```

### 业务线程组大小

在Provider端，实际的远程调用使用一个额定大小的线程池来处理，而合适的线程池大小和服务器的性能有很大关系，为了使框架能灵活的部署在不同配置的服务器上，我们提供了修改这个线程池大小的接口

```
rpc.provider.thread=100 //默认10个
```

### 其他配置

#### 修改Provider监听端口

默认Provider监听2532端口，我们可以在通过修改`rpc.provider.port`来修改这个端口

#### 修改Zookeeper根节点名称

默认使用'rpc'作为根节点名称，可以修改`rpc.register.root`来进行修改

## 修改调用协议

在实际业务场景中，一般还会有一些其他需求，如链路跟踪，权限控制等，这些需求在框架开发阶段，是无法预测的，所以，我们也提供了用户可以进行修改调用链的接口。

在Consumer端和Provider端一个远程调用需要通过如下的调用链：

Consumer：

![](http://ok9ryp7cb.bkt.clouddn.com/2018-06-18-060709.png)

Provider:

![](http://ok9ryp7cb.bkt.clouddn.com/2018-06-18-060803.png)

我们可以在Consumer发送请求之前、发送请求之后、返回结果之前、返回结果之后插入自定义的Invoker，例如，我们需要开发一套权限系统，我们分别需要在Consumer添加一个Invoker用来将权限参数写入请求中，并在Provider添加一个验证Invoker。

Consumer添加验证信息Invoker：

```
public class ConsumerauthInvoker implements Invoker {
    private Invoker next;

    public ConsumerauthInvoker(Invoker next) {
        this.next = next;
    }

    public Object invoke(Object o) throws InvokerException {
        Invocation invocation = (Invocation) o;
        invocation.getRequest().getValues().put("token","token12345678");
        return next.invoke(invocation);
    }
}
```

Provider验证权限Invoker：

```
public class ProviderauthInvoker implements Invoker {
    private Invoker next;

    public ProviderauthInvoker(Invoker next) {
        this.next = next;
    }

    public Object invoke(Object o) throws InvokerException {
        Request request = (Request) o;
        if (request.getValues().get("token").equals("token12345678")) {
            return next.invoke(request);
        }
        throw new InvokerException(new Exception()); //抛出权限错误异常
    }
}
```

在SDK包中添加完这两个Invoker之后，我们需要将这两个Invoker添加至调用链中，我们需要引入ProtocolTemplete，这是一个调用协议的模板类，通过重新这个抽象类的init()方法，我们可以将自定义的Invoker引入调用链中：

```
public class TestProtocol extends ProtocolTemplate {
    protected void init() throws NotInvokerException {
        beforeSend().add(ConsumerauthInvoker.class);

        beforeMethod().add(ProviderauthInvoker.class);
    }
}
```

在这个例子中，我们需要将验证信息的写入放在发送请求之前，所以我们调用beforeSend(),并添加`ConsumerauthInvoker.class` ,在Provider中，我们需要在调用实际接口前验证权限，所以我们调用 beforeMethod()。在这个模板中，一共有7个Invoker切点：

* beforeSend:Consumer 发送请求之前
* afterSend：Consumer发送请求之后
* beforeReturn：Consumer收到远程调用结果之后，返回本地调用之前
* afterReturn：Consumer返回结果之后
* beforeMethod：Provider调用实际接口之前
* afterMethod：Provider调用实际接口之后
* afterResponse：Provider返回响应之后



最后，我们需要在Consumer和Provider的配置文件中启用这个调用链模板：

```
rpc.provider.protocol=test
```

最后的最后，社会人镇个楼

                                                     ^|))1)-'                                       
                                                    .)1+++_){...                                    
                                           ...      ')]++_++)1 .                                    
                                         .1){1))\.  .}1++_++)(.     .. ';-({))1)))1)))^.            
                                         [1_+++_})"  '[{++++(){)11)11?++_++++_1)+++++_1)]..         
                                         _1++++++])l  .\)))))++++++111)(++++++(+++++++++?))'        
                                         .}1_+++++}))1)_?_+++++++().^ .1)_+++-)++++~+_nx++1).       
                                         ..1)_+++1))}_+++{)1{)(_+)}$$$ ')[++++1)+-xx_+nx++_('       
                                            .)))1_+++++_1 ''..))++)1![))-++++++)?++++++++++)^       
                                            `1)+_++++++){p$$ .{1+++++++++++++++_11_+++++++{(..      
                                           (1++++++++++-1)..^})++++++++++++++++++)))?++-)1{.        
                                         .{)+++++++++++++++_+++++++++++++++++++++++++-()]'.         
                                         (1+++++++++++++++++++++++++++++++++++++++_{)1t..           
                                        ()++++{{11{[++++++++++++++++++++++++++++(()),.              
                                        ){++?}{{{1111{-++++++++++++++++++++|))1{^.                  
                                       "1__+}{{{{1111{{_+++++++++++++++++++++)).                    
                                      .1)-++{{{{{111111+++++++++++++++++++++++(-                    
                                      .11++++}{{{11111{++++++++++++++++++X++++{1..                  
                                      .^)-+++++1{111{-_+++<+++++++++++++_Y~+++?1..                  
                                        ))+++++_+__++++++_Xz]++++++++++(z<++++1).                   
                                        <)]+++++++++++++++_?zXUv]-]]vXU-++++++){.                   
                                         })-+++++++++++++++++++_][[___+++++++1).                    
                                         .{)(+++++++++++++++++++++++++++++++))^.                    
                                           .{1}++++++++++++++++++++++++++_1)"...                    
                                           ^QC()1?++++++++++++++++++++_))}UL". .                    
                                          ICQcvcu))1)1]++++++++++?))1))vXvvL0m..                    
                                     ...,-wJccccccccvcv\1)1111{{fvuccvcccccccCJI'..                 
                          ..     .,~~~~_+JLvcccccccccccccccccccccccccccccccccUC0<~~++I.    ....     
                         .<~~~~_~<~<`...ULccccccccccccccccccccccccccccccccccccLQ;. '>~<~><+~<~.     
                          .~~<~+'.    .JLzcccccccccccccccccccccccccccccccccccccLL.     '<<<<~' ..   
                         '+~'~<.      LQXvccccccccccccccccccccccccccccccccccccvcCC      .<<'>~.     
                           . ...      QLvvccccccccccccccccccccccccccccccccccccvvLL`      ........   
                               .,^.  jLcvvvcccccccccccccccccccccccccccccccccccvcuLU..   .           
                             .~<<<~".LLcccccccccccccccccccccccccccccccccccccccvcvCL'.   .           
                          '+<.<<^~~^'LLcccccccccccccccccccccccccccccccccccccccvccUC^.   .           
                          .^+<<~~~~+JLvcccccccccccccccccccccccccccccccccccccccvccvLQ    .           
                             ...   .QCccccccccccccccccccccccccccccccccccccccccvcccLC    .           
                                   'LCvcccccccccccccccccccccccccccccccccccccccvcccCC;   .           
                                   vCXvcvvvvccccccccvvcccvccccvcccccccccccccccccccvCz.              
                                   xJLLLQLLQLQQLLLLLCQLCLLLLLLLLLLLLLLLLLLLLLLLLLLCLL.  
