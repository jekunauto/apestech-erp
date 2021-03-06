package com.apestech.scm;

import com.apestech.oap.AbstractInterceptor;
import com.apestech.oap.RopRequestContext;
import org.springframework.stereotype.Component;


/**
 * <pre>
 *    该拦截器仅对method为“user.add”进行拦截，你可以在{@link #isMatch(com.apestech.oap.RopRequestContext)}方法中定义拦截器的匹配规则。
 *  你可以通过{@link com.apestech.oap.RopRequestContext#getServiceMethodDefinition()}获取服务方法的注解信息，通过这些信息进行拦截匹配规则
 *  定义。
 * </pre>
 *
 */

@Component
public class ReservedUserNameInterceptor extends AbstractInterceptor {

	/**
	 * 在数据绑定后，服务方法调用前执行该拦截方法
	 *
	 * @param ropRequestContext
	 */
	public void beforeService(RopRequestContext ropRequestContext) {
		System.out.println("beforeService ...");
	}

	/**
	 * 在服务执行完成后，响应返回前执行该拦截方法
	 *
	 * @param ropRequestContext
	 */

	public void beforeResponse(RopRequestContext ropRequestContext) {
		System.out.println("beforeResponse ...");
	}

	/**
	 * 对method为user.add的方法进行拦截，你可以通过methodContext中的信息制定拦截方案
	 *
	 * @param ropRequestContext
	 * @return
	 */

	public boolean isMatch(RopRequestContext ropRequestContext) {
		return "user.add".equals(ropRequestContext.getMethod());
	}
}
