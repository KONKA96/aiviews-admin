package com.realm;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.model.Admin;
import com.service.AdminService;

/**
 * Created by Jacey on 2017/6/30.
 */

@Component
public class LoginRealm extends AuthorizingRealm{
	
	@Resource(name="adminServiceImpl")
	private AdminService adminService;

    /**
     * 获取身份信息，我们可以在这个方法中，从数据库获取该用户的权限和角色信息
     *     当调用权限验证时，就会调用此方法
     */
   protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

	   /*Admin admin=new Admin();
	   adminService.adminLogin(admin);
	   
        //通过用户名从数据库获取权限/角色信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> roles=new ArrayList<>();
        roles.add(speakerList.get(0).getRolePower().toString());
        info.addRoles(roles);*/

        return null;
    }

    /**
     * 在这个方法中，进行身份验证
     *         login时调用
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //用户名
        String username = (String) token.getPrincipal();
        //密码
        String password = new String((char[])token.getCredentials());
        //password=new Md5Hash(password, username ,2).toString();
        
        Admin admin=new Admin();
        admin.setUsername(username);
        List<Admin> adminList = adminService.adminLogin(admin);
        if (adminList.size()==0) {
            //没有该用户名
            throw new UnknownAccountException();
        } else if (!password.equals(adminList.get(0).getPassword())) {
            //密码错误
            throw new IncorrectCredentialsException();
        }

        //身份验证通过,返回一个身份信息
        AuthenticationInfo aInfo = new SimpleAuthenticationInfo(username,password,getName());

        return aInfo;
    }
}
