package com.finalproject.tuwaiqfinal;

import com.finalproject.tuwaiqfinal.Controller.OwnerController;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.test.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OwnerControllerStandaloneTest {

    private MockMvc mockMvc;
    private OwnerService ownerService;


    private static final String BASE = "/api/v1/owner";

    private FakeAuthPrincipalResolver authResolver;

    @BeforeEach
    void setup() {
        ownerService = Mockito.mock(OwnerService.class);

        OwnerController controller = new OwnerController(ownerService);

        authResolver = new FakeAuthPrincipalResolver();
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers((org.springframework.web.method.support.HandlerMethodArgumentResolver) authResolver) // يحقن @AuthenticationPrincipal
                .alwaysDo(print())
                .build();
    }

    // ============ GET /get-all ============
    @Test
    void getAllOwnersTest() throws Exception {
        Owner owner1 = new Owner(); owner1.setId(1);
        Owner owner2 = new Owner(); owner2.setId(2);

        when(ownerService.getAllOwners()).thenReturn(List.of(owner1, owner2));

        mockMvc.perform(get(BASE + "/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    // ============ GET /get (requires @AuthenticationPrincipal) ============
    @Test
    void getSingleOwnerTest() throws Exception {
        User fake = fakeUser(10);
        authResolver.setCurrentUser(fake);

        Owner owner = new Owner(); owner.setId(10);
        when(ownerService.getSingleOwner(10)).thenReturn(owner);

        mockMvc.perform(get(BASE + "/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    // ============ POST /register (OwnerDTO @Valid) ============
    @Test
    void registerOwnerTest() throws Exception {
        doNothing().when(ownerService).registerOwner(any());

        String body = """
                {
                  "username": "owner1",
                  "password": "StrongPass!123",
                  "email": "owner1@example.com",
                  "account_serial_num": "ACC-123456"
                }
                """;

        mockMvc.perform(post(BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("owner have been registered"));
    }

    // ============ PUT /update (requires @AuthenticationPrincipal + OwnerDTO) ============
    @Test
    void updateOwnerTest() throws Exception {
        User fake = fakeUser(20);
        authResolver.setCurrentUser(fake);

        doNothing().when(ownerService).updateOwner(Mockito.eq(20), any());

        String body = """
                {
                  "username": "owner20",
                  "password": "NewStrongPass!456",
                  "email": "owner20@example.com",
                  "account_serial_num": "ACC-200200"
                }
                """;

        mockMvc.perform(put(BASE + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("owner have been updated"));
    }

    // ============ DELETE /delete (requires @AuthenticationPrincipal) ============
    @Test
    void deleteOwnerTest() throws Exception {
        User fake = fakeUser(30);
        authResolver.setCurrentUser(fake);

        doNothing().when(ownerService).deleteOwner(30);

        mockMvc.perform(delete(BASE + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("owner have been deleted"));
    }

    // ===== Helpers =====

    private static User fakeUser(int id) {
        User u = new User();
        u.setId(id);
        u.setUsername("u" + id);
        u.setPassword("x"); // irrelevant here
        u.setEmail("u" + id + "@example.com");
        u.setRole("OWNER");
        return u;
    }


    static class FakeAuthPrincipalResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver {
        private final AtomicReference<User> current = new AtomicReference<>();

        void setCurrentUser(User user) {
            current.set(user);
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null
                    && User.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Object resolveArgument(MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) {
            User u = current.get();
            if (u == null) {
                u = new User();
                u.setId(1);
                u.setUsername("default");
                u.setEmail("default@example.com");
                u.setRole("OWNER");
            }
            return u;
        }
    }
}