package com.troy.keeper.system.intercept;

import com.google.common.collect.Lists;
import com.troy.keeper.core.utils.validate.Validate;
import com.troy.keeper.system.intercept.account.AccountAdviceService;

import java.util.List;

/**
 * Created by Harry on 2017/9/6.
 */
public class InterceptService {

    private final static List<AccountAdviceService> ACCOUNT_ADVICE_SERVICES = Lists.newArrayList();

    /**
     * 添加账户service
     * @param accountAdviceService
     */
    public static void addAccountService(AccountAdviceService accountAdviceService) {
        Validate.notNull(accountAdviceService, "accountAdviceService can't be null!");
        if (!ACCOUNT_ADVICE_SERVICES.contains(accountAdviceService)) {
            ACCOUNT_ADVICE_SERVICES.add(accountAdviceService);
        }
    }
}
