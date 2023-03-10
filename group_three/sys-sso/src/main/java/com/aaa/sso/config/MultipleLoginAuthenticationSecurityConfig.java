package com.aaa.sso.config;

import com.aaa.sso.filter.MultipleLoginAuthenticationFilter;
import com.aaa.sso.provider.MultipleLoginAuthenticationProvider;
import com.aaa.sso.service.MyCustomUserDetailsService;
import com.aaa.sso.util.OkHttpClientUtil;
import com.aaa.util.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MultipleLoginAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private MyCustomUserDetailsService myCustomUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(HttpSecurity http) throws Exception {

        MultipleLoginAuthenticationFilter multipleLoginAuthenticationFilter = new MultipleLoginAuthenticationFilter();
        multipleLoginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        multipleLoginAuthenticationFilter.setAuthenticationSuccessHandler(getSuccessHandler());
        multipleLoginAuthenticationFilter.setAuthenticationFailureHandler(getFailtureHandler());

        MultipleLoginAuthenticationProvider multipleLoginAuthenticationProvider = new MultipleLoginAuthenticationProvider();
        multipleLoginAuthenticationProvider.setUserDetailsService(myCustomUserDetailsService);

        multipleLoginAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        //http.authorizeRequests().antMatchers("/oauth/**","/","/login").permitAll();
        http.authenticationProvider(multipleLoginAuthenticationProvider)
                .addFilterBefore(multipleLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


    }

//    v
    /**
     * ????????????
     * @return
     */
    private AuthenticationFailureHandler getFailtureHandler() {
        return (httpServletRequest, response, e) -> {
            Result result = new Result(5000,"????????????",null);
            printJsonData(response,result);
        };
    }

    /**
     * ????????????
     * @return
     */

    public AuthenticationSuccessHandler getSuccessHandler(){
        return (request, response, authentication) -> {

            // ??????token http://localhost:7200/oauth/token?username=admin&password=123456&client_id=client_id&client_secret=secret&grant_type=password

            // java ????????????post??????
            //
            String url="http://localhost:7200/oauth/token";
            Map<String, String> map = new HashMap<>();
            map.put("client_id","client_id");
            map.put("client_secret","secret");
            map.put("grant_type","password");

            // ??????token?????????

// ???????????? ??????????????????
            String username = request.getParameter("telephone");
            String password = request.getParameter("password");
            String loginType = request.getParameter("loginType");
            map.put("username",username);
            map.put("password",password);
            map.put("loginType",loginType);

            Response res = OkHttpClientUtil.getInstance().postData(url, map);
            System.out.println("??????????????????"+res);
            String s = Objects.requireNonNull(res.body().string());
            System.out.println("jsons?????????"+s);
            // ?????? ????????? access_token   JSON->map
            Map<String, Object> map1 = new HashMap<>();
            try {
                map1 = json2map(s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            String o = map1.get("access_token").toString();
            //System.out.println("access_token = " + o);
            String type=map1.get("token_type").toString();
            String token = type + " " + o;
            System.out.println("token = " + token);

            Result result = new Result(token);
            printJsonData(response,result);

        };
    }


    public  void printJsonData(HttpServletResponse response, Result result) throws IOException {
        // ??????????????????.
        response.setContentType("application/json;charset=utf8");
        PrintWriter writer = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        String s1 = objectMapper.writeValueAsString(result);
        writer.print(s1);
        writer.flush();
        writer.close();
    }

    public Map<String, Object> json2map(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }

}
