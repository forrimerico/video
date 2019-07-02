package com.imooc.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * 拦截器
 */
public class MiniInterceptor implements HandlerInterceptor {

    public final static String USER_REDIS_SESSION = "user-resdis-session";

    @Autowired
    private RedisOperator redisOperator;
    /**
     * 调用controller之前调用
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userid = httpServletRequest.getHeader("userId");
        String userToken = httpServletRequest.getHeader("userToken");

        if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redisOperator.get(USER_REDIS_SESSION + ":" + userid);
            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
                returnErrorResponse(httpServletResponse, (new IMoocJSONResult()).errorMsg("请登录"));
                return false;
            } else {
                if (!userToken.equals(uniqueToken)) {
                    returnErrorResponse(httpServletResponse, (new IMoocJSONResult()).errorMsg("账号被挤出"));
                    return false;
                }
            }
        } else {
            returnErrorResponse(httpServletResponse, (new IMoocJSONResult()).errorMsg("请登录"));
            return false;
        }

        return true;
    }

    /**
     * controller之后，渲染视图之前
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求全部结束
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result) throws IOException {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
