해당 Repo는 [여기](https://www.youtube.com/playlist?list=PLOSNUO27qFbsW_JuXmzrFxPw7qzPOFfQs)를 보고 공부하였습니다.

# 공부 기록

프로젝트는 spring5, maven으로 진행.

## 2-1, 2-2
UML entities, services, stores 정의

## 2-3 
pom.xml은 maven의 기본설정을 담고 있다.
그룹id, 아티팩트id, 버전정보,
프로퍼티와 사용하는 라이브러리(dependency)가 있다.

첫번째는 Spring core(IoC를 사용하기 위한 Spring core라이브러리)가 있다.<br>
두번째는 lombok 이며 생산성을 위해 사용하는 도구<br>
set, get 메서드, 생성자 클래스를 만들때 사용하는 코드들.

인텔리제이 우측에 maven항목에 Lifesycle, Plugins, Dependencies가 있다.<br>
Dependencies에 spring-context에 다양한 라이브러리 가 있다.


## 2-4
store.mapstore.ClubMapStore 클래스 구현

## 2-5

### IoC 컨테이너
```java
// ClubServiceLogic.java
public class ClubServiceLogic implements ClubService {
    private ClubStore clubstore;

    public ClubServiceLogic() {
        this.clubStore = new ClubMapStore();
    }
    
    // ...
}
```
위의 방식처럼 작성하면 ClubServiceLogic과 ClubMapStore클래스의 관계가 타이트해진다. (tight coupling)
위의 코드에서 Map에다가 데이터를 저장했는데 DB에 저장하고 싶어 클래스를 변경하게 된다면 (ClubMapStore -> ClubDBStore)
ClubMapStore와 연관된 클래스들을 전부 수정해 주어야 한다.

그래서 이런 부분을 Spring이 가지고 있는 IoC 컨테이너를 이용하면 객체의 생성부터 관계를 구성할 수 있다.(dependencies)
즉, ClubServiceLogic이 ClubMapStore을 알게 하는 이 관계까지도 IoC 컨테이너한테 맡길 수 있다.
(IoC: 제어의 역전 -> 개발자가 new하지 않고 IoC 컨테이너에 의해 new가 돼서 객체도 생성이 된다. 또한 관계들도 구성을 해준다는 의미가 IoC의 내용이다.)

### Spring bean
![img.png](readmeImg/img.png)

객체 생성을 IoC 컨테이너에 맡기려면 그 대상이 되는 클래스들을 등록해야한다.
이렇게 등록된 클래스들을 Spring bean이라고 한다.
(스프링의 IoC 컨테이너에 의해 관리되어지는 클래스들을 bean 클래스라고 한다 = bean객체)

위의 그림을 통해 본 프로젝트를 생각해본다면 bean A는 ClubServiceLogic, bean B는 ClubMapStore, 가운데 참조는 ClubStore 인터페이스를 나타낸다.
ClubServiceLogic에서는 ClubMapStore를 모른다. 
대신 ClubSore의 인터페이스만 알고있는 상태가 된다.
IoC 컨테이너에다가 ClubMapStore를 bean으로 등록을 하면 ClubMapStore를 사용해야되는 시점에 생성하고 그리고 그 생성된 인스턴스 정보를 주입한다
=> 이를 의존관계 주입이라 한다.

IoC컨테이너에 의해 의존관계를 주입하는 방식을 DI방식이라고 한다. 
이 방식은 두가지가 있다 DI가 있고 DL방식이 있다. 
요즘은 DL 방식을 대부분 사용하지 않아 보통 IoC DI라고 이야기한다. 

ClubMapStore를 ClubServiceLogic이 알게 하고 사용할수 있게끔 해주고 싶다면 
첫째 Spring IoC컨테이너로 하여금 ClubMapStore가 bean객체라는 것을 알 수 있도록 등록하는 작업이 필요하다.
둘째 등록된 ClubMapStore는 ClubServiceLogic이 참조해서 사용할 수 있도록 의존관계를 주입해 줘야한다. 

과거 스프링2 버전에서는 applicationContext.xml과 같은 곳에 bean을 생성해서 하나씩 등록해주었다.
최신 스프링5 버전에서는 어노테이션등 과같은 방법을 사용하여 bean을 등록한다
먼저 어떻게 의존성 관계를 주입하는지, Ioc의 이해하기 위해 과거에 사용했던 bean객체를 직접 등록하는과정을 수행해 볼 것이다.
(xml로 어떤 클래스가 무슨id를 갖는 bean이다 라고 일일이 bean을 등록해 주는 방식)

먼저 id를 부여하고 클래스를 지정한다.
```xml
<!-- applicationContext.xml -->
<bean id="clubStore" class="io.travelclub.spring.store.mapstore.ClubMapStore"/>

<bean id="clubService" class="io.travelclub.spring.service.logic.ClubServiceLogic">
<!--    clubServer를 bean으로 등록하면서 생성자의 파라미터로 -->
<!--    clubStore의 아이디를 갖는 bean을 주입하겠다 -->
    <constructor-arg ref="clubStore"/>
</bean>
```
다음과 같이 수정한다. 
```java
// ClubServiceLogic.java
public class ClubServiceLogic implements ClubService {
    private ClubStore clubstore;

    /* ClubServiceLogic이 생성되는 시점에 생성자를 호출하면서 
       ClubMapStore를 new 해서 여기에다가 파라미터로 넘겨 준다. */
    public ClubServiceLogic(ClubStore clubStore) {
        this.clubStore = clubstore;
    }
    
    // ...
}
```
즉, ClubMapStore를 bean으로 등록을 했다.<br>
설정에서 보면 ClubStore, ClubService를 bean으로 등록을 했다.<br>
ClubService가 사용되는 시점에 생성자에서 레퍼런스하고 있는 ClubStore라는 id를 갖는 이 클래스를 주입해 주세요 라는 의미이다.<br>
이 ClubServiceLogic의 입장에서는 이 ClubStore 인터페이스가 참조하고 있는 객체가 ClubMapStore인지 ClubDBStore인지는 상관이 없다.<br>
알아서 주입이 되기 때문이다. <br>
해당 내용은 applicationContext.xml파일이 이 설정의 정보를 보고나서 주입이 된 것이다.
bean 으로 등록할 때는 이렇게 "이 특정 설정 파일에다가 어떤 bean을 쓰겠다."
라고 이렇게 등록을 해주면 IoC 컨테이너가 이 내용을 읽어서
객체의 생성부터 관계의 구성까지 다 해주는 것이다.
이 역할을 IoC 컨테이너가 하는것이다. <br>

(bean 객체를 생성하고 하는 부분들도 BeanFactory 라고 하는 Spring의 라이브러리 중에 특정 BeanFactory 클래스가 그런 작업들을 진행을 한다.)

# 2-6

## bean 객체가 등록되었는지 확인
travelclub 패키지에 테스트를 위한 TravelClubApp 클래스 생성
```java
public class TravelClubApp {
    public static void main(String[] args) {
        // ClassPathXmlApplicationContext정보를 읽어온다. (applicationContext.xml로 설정정보를 읽어오겠다)
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 실제로 bean이 등록되었는지 확인
        String[] beanNames = context.getBeanDefinitionNames();
        // bean의 id들이 출력될 것이다.
        System.out.println(Arrays.toString(beanNames));
        // 실행하면 [clubStore, clubService] 두개의 bean이 이상없이 등록됨을 확인할 수 있다.
    }
}
```
설정하는 방법은 자바클래스 또는 ClassPathXmlApplicationContext 이외의 다른 클래스들도 존재한다.<br> 

그런데 앞서 2-6방법처럼 클래스들을 일일이 bean 태그로 만들기에는 어려움이 있다. <br>
스프링에는 context에 component-scan과 base-package라는 걸 지정해 준다.
```xml
    <!--    base-package가 되는 곳에서부터 하위로 내려가면서 컴포넌드들 즉, bean들을 scan하라는 의미-->
    <context:component-scan base-package="io.travelclub.spring"/>
```
일일이 자기가 bean 으로 등록해주지 않고 특정 패키지부터 bean 들을 찾아라 라는 의미이다.

실행하면 다음 결과가 나온다.
```
[org.springframework.context.annotation.internalConfigurationAnnotationProcessor, 
org.springframework.context.annotation.internalAutowiredAnnotationProcessor, 
org.springframework.context.event.internalEventListenerProcessor, 
org.springframework.context.event.internalEventListenerFactory]
```
아까 등록해두었던 bean들은 등록이 안되어있고 spring framework의 bean들이 출력됨을 확인할 수 있다. <br>
왜냐하면 ClubMapStore 나 ClubServiceLogic 이나 다 bean 으로 등록이 되어야 하는데
이 bean 들을 어떻게 등록하는지를 지정을 안해놓았다.<br>
그래서 ClubMapStore 나 Entity 클래스들이나 지금 모두 다 똑같은 클래스이기 때문에 bean 으로 등록이 안된다.<br> 
이럴 때 bean 으로 등록하는 방법은 bean으로 사용되는 각 클래스에다가 어노테이션을 넣어 준다.
<br><br>
각 클래스 위에 어노테이션을 지정해 보았다.
```java
@Service
public class ClubServiceLogic implements ClubService {
    // ...
}
```

```java
@Repository
public class ClubMapStore implements ClubStore {
    // ...
}

```
이렇게 어노테이션을 등록을 하면<br>
실제로 ClubServiceLogic 과 ClubMapStore 는 Spring bean 으로 등록되는 클래스가 된다. 
```
[clubServiceLogic, 
clubMapStore, 
org.springframework.context.annotation.internalConfigurationAnnotationProcessor, 
org.springframework.context.annotation.internalAutowiredAnnotationProcessor, 
org.springframework.context.event.internalEventListenerProcessor, 
org.springframework.context.event.internalEventListenerFactory]
```
별도의 id 를 지정해 주지 않았기 때문에 실제로 클래스명이 id 로 사용되는 형태를 알 수 있다.<br>
만약 이걸 별도의 id 로 등록하려면, 어노테이션에 문자열로 이름을 넣으면 된다. 
```java
@Repository("ClubStore")
public class ClubMapStore implements ClubStore {
    // ...
}
```

등록이 완료되었으니 사용을 해보자. <br>
실제 service의 create메서드를 호출해서 사용할 것이다.<br>
먼저 clubServiceLogic의 registerClub를 구현한다. 
```java
@Service("clubService")
public class ClubServiceLogic implements ClubService {
    @Override
    public String registerClub(TravelClubCdo club) {
        TravelClub newClub = new TravelClub(club.getName(), club.getIntro());
        newClub.checkValidation();
        return clubStore.create(newClub);
    }
}
```
파라미터에 TravelClubCdo 클래스가 입력된다.<br>
(cdo: Create Domain Object)<br>
(sdo: Service Domain Object) <br>
cdo는 create될 때 필요한 데이터들을 별도의 Domain Object에 나눠놓은 것이다.<br>


```java
public class TravelClubApp {
    public static void main(String[] args) {
        // ClubServiceLogic에 register를 구현하고 TravelClub에서 실제로 register를 하는 과정
        TravelClubCdo clubCdo = new TravelClubCdo("TravelClub", "Test TravelClub");
        // Spring 컨테이너로 하여금 bean 을 생성하게하고 그 bean 을 찾아오는 코드
        ClubService clubService = context.getBean("clubService", ClubService.class);
    }
}
```

### 왜 Clubservice.class를 받아올까? (강의 댓글 참고)
getBean() 메소드는  Spring이 제공하는 BeanFactory가 정의하고 있는 메소드로 getBean(String name), getBean(String name, Class<T> requiredType) 등  5개의 메소드가 오버로딩 되어 있습니다. 이중에서 예제에서 사용한 메소드는 getBean(String name, Class<T> requiredType) 형태의 메소드 입니다. 여기서 궁금하신 부분이 바로 두번째 파라미터 requiredType 일텐데요. getBean() 메소드의 목적은 컨테이너로부터 정확한 빈객체를 가져올 수 있도록 하는 것인데요.

만일 getBean(String name)이라는 메소드를 이용 한다면 "clubService"라는 등록한 빈의 이름만 가지고도 빈객체를 가져올 수 있습니다. 다만, 이렇게 이름만 가지고 빈객체를 요청했을 때 발생할 수 있는 예외는 해당 "이름"으로 등록된 빈이 없거나(NoSuchBeanDefinitionException), 빈 객체를 가져올 수 없는(BeansException) 두 가지 밖에 없습니다.

이렇게 발생할 수 있는 예외를 좀더 세분화해서 만약 빈을 찾지 못했다면, "이름"이 문제인지 아니면 찾아온 빈의 타입이 맞지 않는 것인지로 좀더 세분화 할 수 있게 하기 위해 getBean(String name, Class<T> requiredType) 이 메소드를 사용한 것입니다. 아무래도 이부분에서 헤깔리시는 부분이 ClubServiceLogic.class가 아니고 ClubService.class 인지 이실텐데요. 만약 ClubServiceLogic.class가 되려면 ClubServiceLogic clubService = context.getBean(...)과 같은 코드가 되어야 할텐데 이렇게 되면 빈으로 등록할 필요도 없어지겠죠. 그냥 new를 하면 될테니까요. 따라서 ClubService clubService = context.getBean(...)은 ClubService 인터페이스를 implements한 빈을 반환하는 것이 됩니다. 모든 예제 코드가 그렇지만 ClubService 인터페이스를 implements한 빈 객체는 ClubServiceLogic가 유일합니다.

끝으로 getBean() 메소드에 대한 API 설명 중에서 파라미터에 대한 설명들을 함께 올려드립니다. 이해에 도움이 되셨으면 좋겠습니다.

Parameters :
name - the name of the bean to retrieve
requiredType - type the bean must match; can be an interface or superclass


## 2-7
ClubSerivceLogic클래스의 findClubById, findClubsByName, modify, remove 메서드 구현<br>

mainApp클래스에서 findByClubId까지 해서 실제로 해당 데이터가 존재하는지 확인할 것이다.
```java
public class TravelClubApp {
    public static void main(String[] args) {
        String clubId = clubService.registerClub(clubCdo);

        TravelClub foundecClub = clubService.findClubById(clubId);

        System.out.println("Club name " + foundecClub.getName());
        System.out.println("Club intro " + foundecClub.getIntro());
        System.out.println("Club foundationTime " + new Date(foundecClub.getFoundationTime()));
    }
}
/*output
...
Club name TravelClub
Club intro Test TravelClub
Club foundationTime Tue Jun 28 14:40:36 KST 2022
 */
```
IoC 이해를 위한 bean등록을 한번 더 수행해 볼 것이다.<br>
service 패키지에 MemberServiceLogic 클래스 생성, store패키지에 MemberMapStore 클래스 생성<br>
이후 어노테이션 생성
```java
@Repository
public class MemberMapStore implements MemberStore {}

@Service
public class MemberServiceLogic implements MemberService {}
```
main에서 실행해본다.
```
[clubService, memberServiceLogic, ClubStore, memberMapStore ... ]
```

component-scan 방식으로 bean을 탐색한다.
```xml
<context:component-scan base-package="io.travelclub.spring"/>
```

## 2-8
MemberMapStore 구현