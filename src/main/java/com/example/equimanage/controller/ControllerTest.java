package com.example.equimanage.controller;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.equimanage.pojo.DTO.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * 获取用户列表
     */
    @Test
    public void testLogin() throws Exception {
        String url = "/api/login";
        UserDTO param = new UserDTO();
        param.setPassword("curidemo");
        param.setUsername("bz");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(param)))
                        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        System.out.println(result.getResponse().getStatus() + ":---> " + result.getResponse().getContentAsString());
        //
        param.setPassword("admin123");
        param.setUsername("admin");
        result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(param)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        System.out.println(result.getResponse().getStatus() + ":---> " + result.getResponse().getContentAsString());
    }
}
