package com.apestech.framework.express;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

public class ExpressEngine implements IEngine<String, IExpressContext, Object> {

    private Boolean isCache = true;
    private static boolean isInitialRunner = false;
    private ExpressRunner runner;

    public ExpressEngine() {
        this.runner = new ExpressRunner(true,false);
        initRunner(runner);
    }

    @Override
    public Object execute(String expres, IExpressContext context) throws Exception {
        return runner.execute(initStatement(expres), context, null, isCache, false);
    }
    
    /**
     * 在此处把一些中文符号替换成英文符号
     *
     * @param statement
     * @return
     */
    private String initStatement(String statement) {
        return statement.replace("（", "(").replace("）", ")").replace("；", ";").replace("，", ",").replace("“", "\"").replace("”", "\"");
    }

    private void initRunner(ExpressRunner runner) {
        if (isInitialRunner == true) {
            return;
        }
        synchronized (runner) {
            try {
                //在此可以加入预定义函数
            } catch (Exception e) {
                throw new RuntimeException("初始化失败!", e);
            }
        }
        isInitialRunner = true;
    }    
}
