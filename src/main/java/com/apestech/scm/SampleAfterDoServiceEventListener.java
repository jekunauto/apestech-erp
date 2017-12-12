package com.apestech.scm;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.apestech.oap.RopRequestContext;
import com.apestech.oap.event.AfterDoServiceEvent;
import com.apestech.oap.event.RopEventListener;


public class SampleAfterDoServiceEventListener implements RopEventListener<AfterDoServiceEvent> {

	public void onRopEvent(AfterDoServiceEvent ropEvent) {
		RopRequestContext ropRequestContext = ropEvent.getRopRequestContext();
		if (ropRequestContext != null) {
			Map<String, String> allParams = ropRequestContext.getAllParams();
			System.out.println(JSON.toJSON(allParams));
		}
	}

	public int getOrder() {
		return 0;
	}
}
