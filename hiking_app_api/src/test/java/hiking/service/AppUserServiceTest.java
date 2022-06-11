package hiking.service;

import hiking.models.AppUser;
import hiking.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserServiceTest {

    @Autowired
    AppUserService service;

    @MockBean
    AppUserRepository repository;

    @Test
    void shouldAdd(){
        AppUser user = makeAppUser();
        AppUser output = makeAppUser();
        output.setApp_user_id(1);

        when(repository.add(user)).thenReturn(output);

        Result<AppUser> result = service.add(user);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(output, result.getPayload());
        assertEquals(output.getApp_user_id(), result.getPayload().getApp_user_id());
    }

    @Test
    void shouldNotAcceptDuplicate(){
        AppUser user = makeAppUser();
        AppUser testData = makeAppUser();
        testData.setApp_user_id(1);

        when(repository.findAllUsers()).thenReturn(List.of(testData));

        Result<AppUser> result = service.add(user);

        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddHasIdSet(){
        AppUser user = makeAppUser();
        user.setApp_user_id(1);
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddNullOrBlankEmail(){
        AppUser user = makeAppUser();
        user.setEmail(null);
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setEmail("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddInvalidOrLongEmail(){
        AppUser user = makeAppUser();
        user.setEmail("b".repeat(49)+"@gmail.com");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setEmail("email..email@gmail.com");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setEmail("僕はパトリックアラルコンと申します@tohoku.ac.jp");
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddNullBlankOverMaxPassword(){
        AppUser user = makeAppUser();
        user.setPassword_hash(null);
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setPassword_hash("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setPassword_hash("a".repeat(2049));
    }

    @Test
    void shouldNotAddBlankOrOversizedFirstName(){
        AppUser user = makeAppUser();
        user.setFirst_name("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setFirst_name("a".repeat(51));
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddBlankOrOversizedLastName(){
        AppUser user = makeAppUser();
        user.setLast_name("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setLast_name("a".repeat(51));
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddBlankorOversizedCity(){
        AppUser user = makeAppUser();
        user.setCity("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setCity("a".repeat(51));
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddBlankorOversizedState(){
        AppUser user = makeAppUser();
        user.setState("");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setState("a".repeat(51));
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldNotAddNonUSState(){
        AppUser user = makeAppUser();
        user.setState("ZA");
        assertEquals(ResultType.INVALID, service.add(user).getType());
        user.setState("FR");
        assertEquals(ResultType.INVALID, service.add(user).getType());
    }

    @Test
    void shouldUpdate(){
        AppUser user = makeAppUser();
        user.setApp_user_id(1);

        when(repository.update(user)).thenReturn(true);

        Result<AppUser> result = service.update(user);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdate(){
        AppUser user = makeAppUser();
        user.setApp_user_id(0);

        when(repository.update(user)).thenReturn(false);

        Result<AppUser> result = service.update(user);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldDelete(){
        AppUser user = makeAppUser();
        user.setApp_user_id(1);

        when(repository.delete(user.getApp_user_id())).thenReturn(true);

        Result<AppUser> result = service.delete(user.getApp_user_id());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDelete(){
        AppUser user = makeAppUser();
        user.setApp_user_id(0);

        when(repository.delete(user.getApp_user_id())).thenReturn(false);

        Result<AppUser> result = service.delete(user.getApp_user_id());
        assertEquals(ResultType.INVALID, result.getType());
    }



    private AppUser makeAppUser(){
        return new AppUser(0, "test@apple.com", "newyork", "Lucky", "Luciano", "New York", "NY", true);
    }


}