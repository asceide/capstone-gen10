package hiking.service;

import hiking.models.AppUserInfo;
import hiking.repository.AppUserInfoJdbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserInfoServiceTest {

    @Autowired
    AppUserInfoService service;

    @MockBean
    AppUserInfoJdbcRepository repository;

    @MockBean
    PasswordEncoder encoder;

    @Test
    void shouldFind(){
        AppUserInfo info = makeUserInfo();

        when(repository.findByAppUserId(1)).thenReturn(info);

        assertNotNull(service.findByAppUserId(1));
    }

    @Test
    void shouldAdd(){
        AppUserInfo userInfo = makeUserInfo();

        when(repository.add(userInfo)).thenReturn(userInfo);

        Result<AppUserInfo> result = service.add(userInfo);
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldUpdate(){
        AppUserInfo userInfo = makeUserInfo();

        when(repository.update(userInfo)).thenReturn(true);

        Result<AppUserInfo> result = service.update(userInfo);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDelete(){
        when(repository.delete(1)).thenReturn(true);

        Result<AppUserInfo> result = service.delete(1);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotOperateUserUnderZero() {
        AppUserInfo userInfo = makeUserInfo();
        userInfo.setAppUserId(0);

        assertFalse(service.add(userInfo).isSuccess());;
        assertFalse(service.update(userInfo).isSuccess());
        assertFalse(service.delete(0).isSuccess());
    }

    @Test
    void shouldNotOperateUserWithNull() {
        assertFalse(service.add(null).isSuccess());
        assertFalse(service.update(null).isSuccess());
    }

    @Test
    void shouldNotOperateStateOrCityMustBothBeBlankOrSet(){
        AppUserInfo userInfo = makeUserInfo();
        userInfo.setState("CA");
        userInfo.setCity(null);

        assertFalse(service.add(userInfo).isSuccess());

        userInfo.setState(null);
        userInfo.setCity("Cupertino");
        assertFalse(service.add(userInfo).isSuccess());


        userInfo.setCity(null);
        System.out.println(service.add(userInfo).getMessages());
        assertTrue(service.add(userInfo).isSuccess());


    }

    @Test
    void shouldNotOperateFirstNameLongerThanFiftyChar(){
        AppUserInfo userInfo = makeUserInfo();
        userInfo.setFirstName("abc".repeat(50));

        assertFalse(service.add(userInfo).isSuccess());
    }

    @Test
    void shouldNotOperateLastNameLongerThanFiftyChar(){
        AppUserInfo userInfo = makeUserInfo();
        userInfo.setLastName("abc".repeat(50));

        assertFalse(service.add(userInfo).isSuccess());
    }

    @Test
    void shouldNotOperateStateNotInUS(){
        AppUserInfo userInfo = makeUserInfo();
        userInfo.setState("AF");

        assertFalse(service.add(userInfo).isSuccess());
        assertTrue(service.add(userInfo).getMessages().get(0).contains("US state"));
    }



    private AppUserInfo makeUserInfo(){
        return new AppUserInfo(1, "Steve", "Jobs", "Los Angeles", "CA");
    }
}