package com.game.service;

import com.game.sdk.net.Result;
import com.game.sdk.proto.QuestBankResp;
import com.game.sdk.utils.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class QuestBankService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestBankService.class);


    public Result getQuests(String openId) throws Exception{
        QuestBankResp resp = new QuestBankResp();


        return Result.valueOf(ErrorCode.OK,resp);
    }

}
